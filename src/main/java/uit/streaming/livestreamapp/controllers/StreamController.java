package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.payload.request.CreateStreamRequest;
import uit.streaming.livestreamapp.repository.RoleRepository;
import uit.streaming.livestreamapp.repository.UserRepository;
import uit.streaming.livestreamapp.security.jwt.JwtUtils;
import uit.streaming.livestreamapp.services.UserDetailsImpl;
import uit.streaming.livestreamapp.services.UserDetailsServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/stream")
public class StreamController {

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
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/start")
    @PreAuthorize("hasRole('USER')")
    public String startNewLiveStream(@Valid @RequestHeader("Authorization") String jwt, @RequestBody CreateStreamRequest createStreamRequest) {
        String[] parts = jwt.split(" ");
        String username = jwtUtils.getUserNameFromJwtToken(parts[1]);
        Long user_id = userDetailsService.loadUserByUsername(username).getId();



        return user_id.toString();
    }

}
