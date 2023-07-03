package uit.streaming.livestreamapp.controllers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.entity.RecordVideo;
import uit.streaming.livestreamapp.payload.request.DownloadRecordRequest;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.payload.response.RecordVideoResponse;
import uit.streaming.livestreamapp.repository.RecordVideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/record-video")
public class RecordVideoController {

    @Autowired
    RecordVideoRepository recordVideoRepository;

    @Autowired
    AmazonS3 amazonS3;

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

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

    @GetMapping("/all-record-by-name/{recordName}")
    public ResponseEntity<?> getRecordByStreamId(@PathVariable String recordName) {
        List<RecordVideo> recordVideoList = recordVideoRepository.findAllByRecordName(recordName);

        List<RecordVideoResponse> recordVideoResponses = new ArrayList<>();

        for (RecordVideo recordVideo : recordVideoList) {
            RecordVideoResponse recordVideoResponse = new RecordVideoResponse(recordVideo.getRecordUrl(),
                    recordVideo.getRecordName(),recordVideo.getStartTime(),recordVideo.getEndTime(), recordVideo.getThumbnailUrl(), recordVideo.getStream().getId());
            recordVideoResponses.add(recordVideoResponse);
        }

        return ResponseEntity.ok(recordVideoResponses);
    }

    @GetMapping("/record-by-userid/{userId}")
    public ResponseEntity<?> getAllRecordByUserId(@PathVariable Long userId) {
        List<RecordVideo> recordVideoList = recordVideoRepository.findAllByUserId(userId);

        List<RecordVideoResponse> recordVideoResponses = new ArrayList<>();

        for (RecordVideo recordVideo : recordVideoList) {
            RecordVideoResponse recordVideoResponse = new RecordVideoResponse(recordVideo.getRecordUrl(),
                    recordVideo.getRecordName(),recordVideo.getStartTime(),recordVideo.getEndTime(), recordVideo.getThumbnailUrl(), recordVideo.getStream().getId());
            recordVideoResponses.add(recordVideoResponse);
        }

        return ResponseEntity.ok(recordVideoResponses);
    }

    @PostMapping("/download-by-url/")
    public ResponseEntity<?> downloadRecordByUrl(@RequestBody DownloadRecordRequest downloadRecordRequest) {
        int index = downloadRecordRequest.getVideoUrl().indexOf(".com/");

        String filename = downloadRecordRequest.getVideoUrl().substring(index + 13);
        String objectKey = downloadRecordRequest.getVideoUrl().substring(index + 5);
        String home = System.getProperty("user.home");
        String filePath = home+"/Downloads/" + filename;

        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1"))
                .build();

        // Tải xuống tệp từ Spaces
        S3Object s3Object = s3Client.getObject(new GetObjectRequest("ngant01", objectKey));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        try {
            // Ghi dữ liệu từ luồng vào tệp cục bộ
            File outputFile = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            return ResponseEntity.ok(new MessageResponse("success"));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new MessageResponse("failed"));
        }

    }

}