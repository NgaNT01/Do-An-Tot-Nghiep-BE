package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.RecordVideo;

@Repository
public interface RecordVideoRepository extends JpaRepository<RecordVideo,Long> {
}
