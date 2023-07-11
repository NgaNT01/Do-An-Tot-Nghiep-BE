package uit.streaming.livestreamapp.services;

import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.streaming.livestreamapp.entity.Stream;
import uit.streaming.livestreamapp.payload.response.StreamResponse;
import uit.streaming.livestreamapp.repository.RecordVideoRepository;
import uit.streaming.livestreamapp.repository.StreamRepository;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StreamService {

    @Autowired
    StreamRepository streamRepository;

    @Autowired
    RecordVideoRepository recordVideoRepository;


    @Transactional
    public void stopStreamById(Long streamId, LocalDateTime endTime) {
        streamRepository.stopStreamById(endTime,streamId);
        recordVideoRepository.updateEndTimeRecordVideo(endTime, streamId);
    }

    public List<Stream> getListBroadcastingStreams() {
        List<Stream> streams = streamRepository.getListBroadcastingStreams();
        return streams;
    }

    public int calculateTotalViewsByMonthAndYearAndUserId(int startMonth,int endMonth, int year, Long userId) {
        List<Stream> streams = streamRepository.findByMonthAndYearAndUserId(startMonth,endMonth, year,userId);

        int totalViews = 0;
        for (Stream stream : streams) {
            totalViews += stream.getViewerCount();
        }

        return totalViews;
    }

    public Duration calculateTotalDurationByMonthAndYearAndUserId(int startMonth,int endMonth, int year, Long userId) {
        List<Stream> streams = streamRepository.findByMonthAndYearAndUserId(startMonth,endMonth, year, userId);

        Duration totalDuration = Duration.ZERO;
        for (Stream stream : streams) {
            Duration livestreamDuration = Duration.between(stream.getStartTime(), stream.getEndTime());
            System.out.println(livestreamDuration);
            totalDuration = totalDuration.plus(livestreamDuration);
        }

        System.out.println("total" + totalDuration);

        return totalDuration;
    }

    @Transactional
    public void playStream(Long streamId) {
        Optional<Stream> optionalStream = streamRepository.findById(streamId);
        if (optionalStream.isPresent()) {
            Stream stream = optionalStream.get();

            int prevViewerCount = stream.getViewerCount();
            int nViewerCount = prevViewerCount + 1;
            stream.setViewerCount(nViewerCount);

            streamRepository.save(stream);
        } else {
            throw new NotFoundException("Stream not found with id: " + streamId);
        }
    }

}
