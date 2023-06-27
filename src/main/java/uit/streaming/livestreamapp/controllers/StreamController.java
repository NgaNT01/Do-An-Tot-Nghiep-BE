package uit.streaming.livestreamapp.controllers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
import uit.streaming.livestreamapp.payload.response.UserProfileResponse;
import uit.streaming.livestreamapp.repository.*;
import uit.streaming.livestreamapp.security.jwt.JwtUtils;
import uit.streaming.livestreamapp.services.StreamService;
import uit.streaming.livestreamapp.services.UserDetailsImpl;
import uit.streaming.livestreamapp.services.UserDetailsServiceImpl;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

    @Autowired
    AmazonS3 amazonS3;

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;


    @PostMapping("/start")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> startNewLiveStream(@RequestHeader("Authorization") String jwt, @RequestBody CreateStreamRequest createStreamRequest) {
        String[] parts = jwt.split(" ");
        String username = jwtUtils.getUserNameFromJwtToken(parts[1]);
        User user = userRepository.findByUsername(username);

        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1"))
                .build();

        byte[] byteimage = Base64.getDecoder().decode(createStreamRequest.getStringThumbnail());
        InputStream is = new ByteArrayInputStream(byteimage);
        ObjectMetadata om = new ObjectMetadata();
        om.setContentLength(byteimage.length);
        om.setContentType("image/jpg");

        String filepath = "stream_profile/" + createStreamRequest.getFileName();
        s3client.putObject(
                new PutObjectRequest("ngant01", filepath, is, om)
                        .withCannedAcl(CannedAccessControlList.PublicReadWrite));

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

            stream.setThumbnail(s3client.getUrl("ngant01",filepath).toString());

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

            RecordVideo recordVideo = new RecordVideo(recordUrl,stream.getStreamName(),startTime,stream.getThumbnail());

            recordVideo.setStream(stream);
            recordVideoRepository.save(recordVideo);
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),stream.getCategories(),stream.getThumbnail(),stream.getStartTime(),stream.getStatus(),stream.getUser().getId());

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
                stream.get().getCategories(),stream.get().getThumbnail(),stream.get().getStartTime(),stream.get().getEndTime(),stream.get().getStatus(),stream.get().getUser().getId());

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
                    stream.getCategories(),stream.getThumbnail(),stream.getStartTime(),stream.getStatus(),stream.getUser().getId());

            listBroadcastingStreams.add(streamResponse);
        }
        return ResponseEntity.ok(listBroadcastingStreams);
    }

    @GetMapping("/stream-by-username/{username}")
    public ResponseEntity<?> getStreamByUserName(@PathVariable String username) {
        Stream stream = streamRepository.findStreamByUserName(username);

        if (stream != null) {
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription()
                    ,stream.getCategories(),stream.getThumbnail(),stream.getStartTime(),stream.getStatus(),stream.getUser().getId());
            return ResponseEntity.ok(streamResponse);
        }
        else {
            return ResponseEntity.ok(new MessageResponse("Streamer chưa phát trực tuyến"));
        }


    }

    @GetMapping("/stream-by-category/{category}")
    public ResponseEntity<List<StreamResponse>> getListBroadcastingStreamsByCategory(@PathVariable String category) {
        List<StreamResponse> listBroadcastingStreamsByCategory = new ArrayList<>();
        for (Stream stream : streamRepository.getListBroadcastingStreamsByCategory(category)) {
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),
                    stream.getCategories(),stream.getThumbnail(),stream.getStartTime(),stream.getStatus(),stream.getUser().getId());
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
                    stream.getCategories(),stream.getThumbnail(),stream.getStartTime(),stream.getStatus(),stream.getUser().getId());
            streamResponses.add(streamResponse);
        }
        return ResponseEntity.ok(streamResponses);
    }

    @GetMapping("/all-stream-by-name/{streamName}")
    public ResponseEntity<?> getAllStreamByName(@PathVariable String streamName) {
        List<Stream> streamList= streamRepository.getAllStreamByStreamName(streamName);
        List<StreamResponse> streamResponseList = new ArrayList<>();
        for (Stream stream : streamList) {
            StreamResponse streamResponse = new StreamResponse(stream.getId(),stream.getStreamName(),stream.getDescription(),
                    stream.getCategories(),stream.getThumbnail(),stream.getStartTime(),stream.getStatus(),stream.getUser().getId());
            streamResponseList.add(streamResponse);
        }
        return ResponseEntity.ok(streamResponseList);
    }

}
