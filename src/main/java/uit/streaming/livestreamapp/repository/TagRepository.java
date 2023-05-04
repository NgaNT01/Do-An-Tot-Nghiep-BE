package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Category;

@Repository
public interface TagRepository extends JpaRepository<Category,Long> {
}
