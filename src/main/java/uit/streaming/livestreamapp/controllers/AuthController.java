package uit.streaming.livestreamapp.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.streaming.livestreamapp.entity.ERole;
import uit.streaming.livestreamapp.entity.Follow;
import uit.streaming.livestreamapp.entity.Role;
import uit.streaming.livestreamapp.entity.User;
import uit.streaming.livestreamapp.payload.request.ChangeAvatarRequest;
import uit.streaming.livestreamapp.payload.request.LoginRequest;
import uit.streaming.livestreamapp.payload.request.SignupRequest;
import uit.streaming.livestreamapp.payload.response.JwtResponse;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.payload.response.UserProfileResponse;
import uit.streaming.livestreamapp.repository.FollowRepository;
import uit.streaming.livestreamapp.repository.RoleRepository;
import uit.streaming.livestreamapp.repository.UserRepository;
import uit.streaming.livestreamapp.security.jwt.JwtUtils;
import uit.streaming.livestreamapp.services.UserDetailsImpl;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AmazonS3 amazonS3;

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAvatar_url(),
                roles);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getAvatarUrl(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/upload-avatar")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ResponseEntity<?> uploadAvatar(@RequestBody ChangeAvatarRequest changeAvatarRequest) throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1"))
                .build();

        byte[] byteAvatarImage = Base64.getDecoder().decode(changeAvatarRequest.getStringAvatar());
        InputStream is = new ByteArrayInputStream(byteAvatarImage);
        ObjectMetadata om = new ObjectMetadata();
        om.setContentLength(byteAvatarImage.length);
        om.setContentType("image/jpg");

        String filepath = "user_profile/" + changeAvatarRequest.getAvatarFileName();
        s3client.putObject(
                new PutObjectRequest("ngant01", filepath, is, om)
                        .withCannedAcl(CannedAccessControlList.PublicReadWrite));

        userRepository.setUserAvatarUrl(s3client.getUrl("ngant01",filepath).toString(), changeAvatarRequest.getUserId());

        Optional<User> user = userRepository.findById(changeAvatarRequest.getUserId());

        List<Follow> followList = followRepository.getListFollowerByFollowing(user.get().getId());

        UserProfileResponse userProfileResponse = new UserProfileResponse(user.get().getId(),user.get().getUsername(),user.get().getEmail(),
                user.get().getAvatarUrl(),user.get().getLive(),followList.size(),user.get().getRoles(),user.get().getStreams());
        return ResponseEntity.ok(userProfileResponse);
    }

    @GetMapping("/user-profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfileById(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);

        List<Follow> followList = followRepository.getListFollowerByFollowing(user.get().getId());

        UserProfileResponse userProfileResponse = new UserProfileResponse(user.get().getId(),user.get().getUsername(),user.get().getEmail(),
                user.get().getAvatarUrl(),user.get().getLive(),followList.size(),user.get().getRoles(),user.get().getStreams());

        return ResponseEntity.ok(userProfileResponse);
    }

    @GetMapping("/user-by-name/{username}")
    public ResponseEntity<UserProfileResponse> getUserProfileByName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        List<Follow> followList = followRepository.getListFollowerByFollowing(user.getId());

        UserProfileResponse userProfileResponse = new UserProfileResponse(user.getId(),user.getUsername(),user.getEmail(),
                user.getAvatarUrl(),user.getLive(),followList.size(),user.getRoles(),user.getStreams());

        return ResponseEntity.ok(userProfileResponse);
    }

    @GetMapping("/all-user-by-name/{username}")
    public ResponseEntity<?> getAllUserByName(@PathVariable String username) {
        List<User> userList= userRepository.findAllByUsername(username);
        List<UserProfileResponse> userProfileResponseList = new ArrayList<>();
        for (User user : userList) {
            List<Follow> followList = followRepository.getListFollowerByFollowing(user.getId());
            UserProfileResponse userProfileResponse = new UserProfileResponse(user.getId(),user.getUsername(),user.getEmail(),
                    user.getAvatarUrl(),user.getLive(),followList.size(),user.getRoles(),user.getStreams());
            userProfileResponseList.add(userProfileResponse);
        }
        return ResponseEntity.ok(userProfileResponseList);
    }
}