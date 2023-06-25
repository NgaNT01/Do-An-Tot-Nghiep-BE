package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
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
import uit.streaming.livestreamapp.payload.response.RecordVideoResponse;
import uit.streaming.livestreamapp.payload.response.StatisticResponse;
import uit.streaming.livestreamapp.payload.response.StreamResponse;
import uit.streaming.livestreamapp.repository.CategoryRepository;
import uit.streaming.livestreamapp.repository.RecordVideoRepository;
import uit.streaming.livestreamapp.repository.StreamRepository;
import uit.streaming.livestreamapp.repository.UserRepository;
import uit.streaming.livestreamapp.security.jwt.JwtUtils;
import uit.streaming.livestreamapp.services.StreamService;
import uit.streaming.livestreamapp.services.UserDetailsServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/record-video")
public class RecordVideoController {

    @Autowired
    RecordVideoRepository recordVideoRepository;

    @GetMapping("/record-by-category/{category}")
    public ResponseEntity<?> getListRecordByCategory(@PathVariable String category) {
       List<RecordVideo> recordVideoList = recordVideoRepository.getListRecordVideoByCategory(category);

       List<RecordVideoResponse> recordVideoResponseList = new ArrayList<>();

       for (RecordVideo recordVideo : recordVideoList) {
           RecordVideoResponse recordVideoResponse = new RecordVideoResponse(recordVideo.getRecordUrl(),
                   recordVideo.getRecordName(),recordVideo.getStartTime(),recordVideo.getEndTime(), recordVideo.getThumbnailUrl(), recordVideo.getStream().getId());
           recordVideoResponseList.add(recordVideoResponse);
       }

       return ResponseEntity.ok(recordVideoResponseList);
    }

    @GetMapping("/record-by-streamid/{streamId}")
    public ResponseEntity<?> getRecordByStreamId(@PathVariable Long streamId) {
        RecordVideo recordVideo = recordVideoRepository.findRecordVideoByStreamId(streamId);

        RecordVideoResponse recordVideoResponse = new RecordVideoResponse(recordVideo.getRecordUrl(),
                recordVideo.getRecordName(),recordVideo.getStartTime(),recordVideo.getEndTime(), recordVideo.getThumbnailUrl(), recordVideo.getStream().getId());

        return ResponseEntity.ok(recordVideoResponse);
    }

}