package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Modifying
    @Query(value = "DELETE FROM Follow f WHERE f.follower = ?1 and f.following = ?2")
    public void deleteByFollowerIdAndFollowingId(Long followerId,Long followingId);

}
