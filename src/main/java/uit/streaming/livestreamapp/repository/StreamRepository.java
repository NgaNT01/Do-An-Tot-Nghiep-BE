package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Stream;

@Repository
public interface StreamRepository extends JpaRepository<Stream,Long> {
}
