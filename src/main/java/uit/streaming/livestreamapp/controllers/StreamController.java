package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.entity.Category;
import uit.streaming.livestreamapp.entity.Stream;
import uit.streaming.livestreamapp.entity.User;
import uit.streaming.livestreamapp.payload.request.CreateStreamRequest;
import uit.streaming.livestreamapp.payload.request.StopStreamRequest;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.payload.response.StreamResponse;
import uit.streaming.livestreamapp.repository.CategoryRepository;
import uit.streaming.livestreamapp.repository.RoleRepository;
import uit.streaming.livestreamapp.repository.StreamRepository;
import uit.streaming.livestreamapp.repository.UserRepository;
import uit.streaming.livestreamapp.security.jwt.JwtUtils;
import uit.streaming.livestreamapp.services.StreamService;
import uit.streaming.livestreamapp.services.UserDetailsImpl;
import uit.streaming.livestreamapp.services.UserDetailsServiceImpl;

import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/stream")
public class StreamController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    StreamRepository streamRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    StreamService streamService;


    @PostMapping("/start")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StreamResponse> startNewLiveStream(@RequestHeader("Authorization") String jwt, @RequestBody CreateStreamRequest createStreamRequest) {
        String[] parts = jwt.split(" ");
        String username = jwtUtils.getUserNameFromJwtToken(parts[1]);
        User user = userRepository.findByUsername(username);

        Stream stream = new Stream(createStreamRequest.getStreamName(),
                createStreamRequest.getDescription(),
                createStreamRequest.getStatus());

        stream.setUser(user);
        Set<String> strCategories = createStreamRequest.getCategories();
        Set<Category> categories = new HashSet<>();

        if (strCategories == null) {
            Category category = categoryRepository.findCategoryByName("Games");
            categories.add(category);
        }
        else {
            strCategories.forEach(category -> {
                Category category1 = categoryRepository.findCategoryByName(category);
                categories.add(category1);
            });
        }

        stream.setCategories(categories);
        streamRepository.save(stream);

        StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),stream.getCategories(),stream.getStatus(),stream.getUser().getId());

        return ResponseEntity.ok(streamResponse);
    }

    @PostMapping("/stop")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StreamResponse> stopLiveStream(@RequestBody StopStreamRequest stopStreamRequest) {
        streamService.stopStreamById(stopStreamRequest.getStreamId());

        Optional<Stream> stream = streamRepository.findById(stopStreamRequest.getStreamId());

        StreamResponse streamResponse = new StreamResponse(stream.get().getId(),stream.get().getStreamName(),stream.get().getDescription(),
                stream.get().getCategories(),stream.get().getStatus(),stream.get().getUser().getId());

        return ResponseEntity.ok(streamResponse);
    }
    
    @GetMapping("/broadcasting-stream")
    public ResponseEntity<List<StreamResponse>> getListBroadcastingStream() {
        List<StreamResponse> listBroadcastingStreams = new ArrayList<>();
        for (Stream stream : streamRepository.getListBroadcastingStreams()) {
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),
                    stream.getCategories(),stream.getStatus(),stream.getUser().getId());
            listBroadcastingStreams.add(streamResponse);
        }
        return ResponseEntity.ok(listBroadcastingStreams);
    }

}
