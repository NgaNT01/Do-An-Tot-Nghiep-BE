package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.RecordVideo;
import uit.streaming.livestreamapp.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordVideoRepository extends JpaRepository<RecordVideo,Long> {

    @Query(value = "SELECT * FROM record_video r WHERE r.stream_id = ?1",nativeQuery = true)
    public RecordVideo findRecordVideoByStreamId(Long streamId);

    @Modifying
    @Query(value = "UPDATE record_video r SET r.end_time = ?1 WHERE r.stream_id = ?2",nativeQuery = true)
    public void updateEndTimeRecordVideo(LocalDateTime endTime, Long streamId);

    @Query(value = "SELECT rc.* FROM STREAM s inner join category_stream cs on s.id = cs.stream_id " +
            "inner join categories c on c.id = cs.category_id inner join record_video rc on rc.stream_id = s.id " +
            "where c.name = ?1 and rc.end_time is not null", nativeQuery = true)
    public List<RecordVideo> getListRecordVideoByCategory(String category);

    @Query(value = "SELECT * FROM record_video rc WHERE rc.record_name LIKE concat('%', ?1, '%')",nativeQuery = true)
    List<RecordVideo> findAllByRecordName(String recordName);

}
