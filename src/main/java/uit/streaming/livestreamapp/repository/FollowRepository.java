package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Follow;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Modifying
    @Query(value = "DELETE FROM Follow f WHERE f.follower_id = ?1 and f.following_id = ?2",nativeQuery = true)
    public void deleteByFollowerIdAndFollowingId(Long followerId,Long followingId);

    @Query(value = "SELECT * FROM Follow f where f.follower_id = ?1", nativeQuery = true)
    public List<Follow> getListFollowingByFollower(Long followerId);

}
