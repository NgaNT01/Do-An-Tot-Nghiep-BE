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

    @Query(value = "SELECT s.* FROM stream s WHERE MONTH(s.start_time) = ?1 AND YEAR(s.start_time) = ?2 AND s.user_id = ?3 AND s.status = 'stopped'",
            nativeQuery = true)
    List<Stream> findByMonthAndYearAndUserId(int month, int year, Long userId);

    @Modifying
    @Query(value = "UPDATE Stream s SET s.status = 'stopped', s.end_time = ?1 WHERE s.id = ?2",nativeQuery = true)
    public void stopStreamById(LocalDateTime endTime,Long id);

    @Query(value = "SELECT s FROM Stream s WHERE s.status = 'broadcasting'")
    public List<Stream> getListBroadcastingStreams();

    @Query(value = "SELECT s.* FROM STREAM s inner join users u " +
            "on s.user_id = u.id where s.status = 'broadcasting' and u.username = ?1",nativeQuery = true)
    Stream findStreamByUserName(String username);

    @Query(value = "select * from stream s inner join category_stream  cs on s.id = cs.stream_id inner join categories c on c.id = cs.category_id where c.name = ?1 and s.status = 'broadcasting'", nativeQuery = true)
    public List<Stream> getListBroadcastingStreamsByCategory(String category);

}
