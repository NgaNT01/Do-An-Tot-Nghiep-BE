package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.entity.Follow;
import uit.streaming.livestreamapp.entity.Stream;
import uit.streaming.livestreamapp.entity.User;
import uit.streaming.livestreamapp.payload.request.FollowRequest;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.payload.response.UserProfileResponse;
import uit.streaming.livestreamapp.repository.FollowRepository;
import uit.streaming.livestreamapp.repository.StreamRepository;
import uit.streaming.livestreamapp.repository.UserRepository;
import uit.streaming.livestreamapp.services.FollowService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    StreamRepository streamRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> followUser(@RequestBody FollowRequest followRequest) {

        followService.followUser(followRequest.getFollowerId(), followRequest.getFollowingId());

        return ResponseEntity.ok( new MessageResponse("Follow thanh cong"));
    }

    @PostMapping("/check-follow")
    public ResponseEntity<?> checkFollow(@RequestBody FollowRequest followRequest) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followRequest.getFollowerId(),
                followRequest.getFollowingId())) {
            return ResponseEntity.ok(new MessageResponse("Chua follow"));
        }
        else return ResponseEntity.ok(new MessageResponse("Da follow"));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollow(@RequestBody FollowRequest followRequest) {
        followService.unfollowUser(followRequest.getFollowerId(), followRequest.getFollowingId());
        return ResponseEntity.ok(new MessageResponse("Da unfollow"));
    }

    @GetMapping("/list-following/{followerId}")
    public ResponseEntity<?> getListFollowingByFollower(@PathVariable Long followerId) {
        List<Follow> followList = followRepository.getListFollowingByFollower(followerId);
        List<UserProfileResponse> userProfileResponseList = new ArrayList<>();
        for (Follow follow : followList) {
            Optional<User> followingUser = userRepository.findById(follow.getFollowing().getId());
            UserProfileResponse userProfileResponse = new UserProfileResponse(followingUser.get().getId(),
                    followingUser.get().getUsername(),followingUser.get().getEmail(),followingUser.get().getAvatarUrl(),
                    followingUser.get().getLive(),followingUser.get().getRoles(),followingUser.get().getStreams());
            userProfileResponseList.add(userProfileResponse);
        }
        return ResponseEntity.ok(userProfileResponseList);
    }

}
