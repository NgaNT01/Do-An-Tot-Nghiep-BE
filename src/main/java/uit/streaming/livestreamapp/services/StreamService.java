package uit.streaming.livestreamapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.streaming.livestreamapp.entity.Stream;
import uit.streaming.livestreamapp.payload.response.StreamResponse;
import uit.streaming.livestreamapp.repository.StreamRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StreamService {

    @Autowired
    StreamRepository streamRepository;


    @Transactional
    public void stopStreamById(Long streamId) {
        streamRepository.stopStreamById(streamId);
    }

    public List<Stream> getListBroadcastingStreams() {
        List<Stream> streams = streamRepository.getListBroadcastingStreams();
        return streams;
    }

}
