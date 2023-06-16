package uit.streaming.livestreamapp.controllers;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.streaming.livestreamapp.entity.ERole;
import uit.streaming.livestreamapp.entity.Role;
import uit.streaming.livestreamapp.entity.User;
import uit.streaming.livestreamapp.payload.request.LoginRequest;
import uit.streaming.livestreamapp.payload.request.SignupRequest;
import uit.streaming.livestreamapp.payload.response.JwtResponse;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.payload.response.UserProfileResponse;
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
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private AmazonS3 amazonS3;

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
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") Long userId) throws IOException {
        String fileName = file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String key = "avatar/" + userId + "/" + fileName;

        PutObjectRequest putObjectRequest = new PutObjectRequest("ngant01", key, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(putObjectRequest);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvatarUrl("https://ngant01.sgp1.digitaloceanspaces.com/" + key);

        userRepository.save(user);

        return ResponseEntity.ok("Upload avatar successfully");
    }

    @GetMapping("/user-profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfileById(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);

        UserProfileResponse userProfileResponse = new UserProfileResponse(user.get().getId(),user.get().getUsername(),user.get().getEmail(),
                user.get().getAvatarUrl(),user.get().getLive(),user.get().getRoles(),user.get().getStreams());

        return ResponseEntity.ok(userProfileResponse);
    }

    @GetMapping("/user-by-name/{username}")
    public ResponseEntity<UserProfileResponse> getUserProfileByName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        UserProfileResponse userProfileResponse = new UserProfileResponse(user.getId(),user.getUsername(),user.getEmail(),
                user.getAvatarUrl(),user.getLive(),user.getRoles(),user.getStreams());

        return ResponseEntity.ok(userProfileResponse);
    }
}