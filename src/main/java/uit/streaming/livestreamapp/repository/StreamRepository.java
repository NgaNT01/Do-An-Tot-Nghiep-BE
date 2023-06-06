package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Stream;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long> {

    Stream findStreamByStreamName(String streamName);

    @Modifying
    @Query(value = "UPDATE Stream s SET s.status = 'stopped' WHERE s.id = ?1")
    public void stopStreamById(Long id);

    @Query(value = "SELECT s FROM Stream s WHERE s.status = 'broadcasting'")
    public List<Stream> getListBroadcastingStreams();

    @Query(value = "SELECT s.* FROM STREAM s inner join users u " +
            "on s.user_id = u.id where s.status = 'broadcasting' and u.username = ?1",nativeQuery = true)
    Stream findStreamByUserName(String username);

}
