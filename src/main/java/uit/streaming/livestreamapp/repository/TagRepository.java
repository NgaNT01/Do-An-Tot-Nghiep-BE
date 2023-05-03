package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
}
