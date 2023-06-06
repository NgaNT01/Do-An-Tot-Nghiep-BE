package uit.streaming.livestreamapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Optional<User> findById(Long userId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "select count(*) from users u inner join stream s " +
            "on u.id = s.user_id where s.status = 'broadcasting' and u.id = ?1", nativeQuery = true)
    Integer numberOfBroadcastingStream(Long userId);
}
