package uit.streaming.livestreamapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.streaming.livestreamapp.entity.Follow;
import uit.streaming.livestreamapp.entity.User;
import uit.streaming.livestreamapp.repository.FollowRepository;
import uit.streaming.livestreamapp.repository.UserRepository;

import java.util.Optional;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    public void followUser(Long followerId, Long followingId) {
        // Kiểm tra xem đã tồn tại mối quan hệ theo dõi hay chưa
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {

            // Lấy thông tin người theo dõi
            Optional<User> follower = userRepository.findById(followerId);
            User followerEntity = follower.get();

            // Lấy thông tin người được theo dõi
            Optional<User> following = userRepository.findById(followingId);
            User followingEntity = following.get();

            Follow follow = new Follow(followerEntity,followingEntity);

            followRepository.save(follow);
        }
    }

    public void unfollowUser(Long followerId, Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

}
