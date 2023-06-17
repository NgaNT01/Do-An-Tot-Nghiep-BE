package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.entity.Category;
import uit.streaming.livestreamapp.entity.RecordVideo;
import uit.streaming.livestreamapp.entity.Stream;
import uit.streaming.livestreamapp.entity.User;
import uit.streaming.livestreamapp.payload.request.CreateStreamRequest;
import uit.streaming.livestreamapp.payload.request.GetStatisticRequest;
import uit.streaming.livestreamapp.payload.request.StopStreamRequest;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.payload.response.StatisticResponse;
import uit.streaming.livestreamapp.payload.response.StreamResponse;
import uit.streaming.livestreamapp.repository.*;
import uit.streaming.livestreamapp.security.jwt.JwtUtils;
import uit.streaming.livestreamapp.services.StreamService;
import uit.streaming.livestreamapp.services.UserDetailsImpl;
import uit.streaming.livestreamapp.services.UserDetailsServiceImpl;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
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
    RecordVideoRepository recordVideoRepository;

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
    public ResponseEntity<?> startNewLiveStream(@RequestHeader("Authorization") String jwt, @RequestBody CreateStreamRequest createStreamRequest) {
        String[] parts = jwt.split(" ");
        String username = jwtUtils.getUserNameFromJwtToken(parts[1]);
        User user = userRepository.findByUsername(username);

        if (userRepository.numberOfBroadcastingStream(user.getId()) < 1) {
            LocalDateTime startTime = LocalDateTime.now();

            Stream stream = new Stream(createStreamRequest.getStreamName(),
                    createStreamRequest.getDescription(),
                    createStreamRequest.getStatus(),startTime);

            if (createStreamRequest.getAccessCode() != null) {
                stream.setPublic(false);
                stream.setAccessCode(createStreamRequest.getAccessCode());
            }
            else {
                stream.setPublic(true);
            }

            stream.setViewerCount(0);

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
            String recordUrl = "https://ngant01.sgp1.digitaloceanspaces.com/streams/" + username + "_" + stream.getId().toString() +
                    ".mp4";

            RecordVideo recordVideo = new RecordVideo(recordUrl,stream.getStreamName(),startTime);

            recordVideo.setStream(stream);
            recordVideoRepository.save(recordVideo);
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),stream.getCategories(),stream.getStartTime(),stream.getStatus(),stream.getUser().getId());

            return ResponseEntity.ok(streamResponse);
        }
        else {
            return ResponseEntity.ok(new MessageResponse("Error"));
        }
    }

    @PostMapping("/stop")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StreamResponse> stopLiveStream(@RequestBody StopStreamRequest stopStreamRequest) {
        LocalDateTime endTime = LocalDateTime.now();
        streamService.stopStreamById(stopStreamRequest.getStreamId(),endTime);

        Optional<Stream> stream = streamRepository.findById(stopStreamRequest.getStreamId());

        StreamResponse streamResponse = new StreamResponse(stream.get().getId(),stream.get().getStreamName(),stream.get().getDescription(),
                stream.get().getCategories(),stream.get().getStartTime(),stream.get().getEndTime(),stream.get().getStatus(),stream.get().getUser().getId());

        return ResponseEntity.ok(streamResponse);
    }

    @PostMapping("/statistic")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getStatisticInfo(@RequestBody GetStatisticRequest getStatisticRequest) {
        int totalViews = streamService.calculateTotalViewsByMonthAndYearandUserId(getStatisticRequest.getMonth(), getStatisticRequest.getYear(), getStatisticRequest.getUserId());
        Long totalDurations = streamService.calculateTotalDurationByMonthAndYearAndUserId(getStatisticRequest.getMonth(), getStatisticRequest.getYear(), getStatisticRequest.getUserId()).toSeconds();

        StatisticResponse statisticResponse = new StatisticResponse(totalViews,totalDurations);

        return ResponseEntity.ok(statisticResponse);
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

    @GetMapping("/stream-by-username/{username}")
    public ResponseEntity<StreamResponse> getStreamByUserName(@PathVariable String username) {
        Stream stream = streamRepository.findStreamByUserName(username);

        StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription()
                ,stream.getCategories(),stream.getStatus(),stream.getUser().getId());

        return ResponseEntity.ok(streamResponse);
    }

    @GetMapping("/stream-by-category/{category}")
    public ResponseEntity<List<StreamResponse>> getListBroadcastingStreamsByCategory(@PathVariable String category) {
        List<StreamResponse> listBroadcastingStreamsByCategory = new ArrayList<>();
        for (Stream stream : streamRepository.getListBroadcastingStreamsByCategory(category)) {
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),
                    stream.getCategories(),stream.getStatus(),stream.getUser().getId());
            listBroadcastingStreamsByCategory.add(streamResponse);
        }
        return ResponseEntity.ok(listBroadcastingStreamsByCategory);
    }

    @GetMapping("/get-all-stream")
    public ResponseEntity<?> getAllStream() {
        List<Stream> streams = streamRepository.getListStream();
        List<StreamResponse> streamResponses = new ArrayList<>();
        for (Stream stream : streams) {
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),
                    stream.getCategories(),stream.getStatus(),stream.getUser().getId());
            streamResponses.add(streamResponse);
        }
        return ResponseEntity.ok(streamResponses);
    }

}
