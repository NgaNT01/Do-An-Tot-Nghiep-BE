package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Stream;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long> {

    Stream findStreamByStreamName(String streamName);

    @Query(value = "SELECT s.* FROM stream s WHERE MONTH(s.start_time) BETWEEN ?1 AND ?2 AND YEAR(s.start_time) = ?3 AND s.user_id = ?4 AND s.status = 'stopped'",
            nativeQuery = true)
    List<Stream> findByMonthAndYearAndUserId(int startMonth,int endMonth, int year, Long userId);

    @Modifying
    @Query(value = "UPDATE Stream s SET s.status = 'stopped', s.end_time = ?1 WHERE s.id = ?2",nativeQuery = true)
    public void stopStreamById(LocalDateTime endTime,Long id);

    @Modifying
    @Query(value = "UPDATE Stream s SET s.status = 'stopped', s.end_time = ?1 WHERE s.id = ?2",nativeQuery = true)
    public void updateStreamById();

    @Query(value = "SELECT s.* FROM Stream s WHERE s.status = 'broadcasting'",nativeQuery = true)
    public List<Stream> getListBroadcastingStreams();

    @Query(value = "SELECT s.* FROM STREAM s inner join users u " +
            "on s.user_id = u.id where s.status = 'broadcasting' and u.username = ?1",nativeQuery = true)
    Stream findStreamByUserName(String username);

    @Query(value = "select * from stream s inner join category_stream  cs on s.id = cs.stream_id inner join categories c on c.id = cs.category_id where c.name = ?1 and s.status = 'broadcasting'", nativeQuery = true)
    public List<Stream> getListBroadcastingStreamsByCategory(String category);

    @Query(value = "SELECT * FROM stream",nativeQuery = true)
    public List<Stream> getListStream();

    @Query(value = "SELECT * FROM stream s WHERE s.stream_name LIKE concat('%', ?1, '%') and s.status = 'broadcasting'",nativeQuery = true)
    public List<Stream> getAllStreamByStreamName(String streamName);

    @Modifying
    @Query(value = "UPDATE Stream s SET s.viewer_count = ?1 where s.id = ?2",nativeQuery = true)
    public void updateViewerCountById(int viewerCount, Long streamId);

}
