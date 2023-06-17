package uit.streaming.livestreamapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.streaming.livestreamapp.entity.Stream;
import uit.streaming.livestreamapp.payload.response.StreamResponse;
import uit.streaming.livestreamapp.repository.RecordVideoRepository;
import uit.streaming.livestreamapp.repository.StreamRepository;

import javax.transaction.Transactional;
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

    public int calculateTotalViewsByMonthAndYearandUserId(int month, int year, Long userId) {
        List<Stream> streams = streamRepository.findByMonthAndYearAndUserId(month, year,userId);

        int totalViews = 0;
        for (Stream stream : streams) {
            totalViews += stream.getViewerCount();
        }

        return totalViews;
    }

    public Duration calculateTotalDurationByMonthAndYearAndUserId(int month, int year, Long userId) {
        List<Stream> streams = streamRepository.findByMonthAndYearAndUserId(month, year, userId);

        Duration totalDuration = Duration.ZERO;
        for (Stream stream : streams) {
            Duration livestreamDuration = Duration.between(stream.getStartTime(), stream.getEndTime());
            System.out.println(livestreamDuration);
            totalDuration = totalDuration.plus(livestreamDuration);
        }

        System.out.println("total" + totalDuration);

        return totalDuration;
    }

}
