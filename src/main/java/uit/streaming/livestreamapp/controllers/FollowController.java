package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.entity.Follow;
import uit.streaming.livestreamapp.entity.Stream;
import uit.streaming.livestreamapp.payload.request.FollowRequest;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.repository.StreamRepository;
import uit.streaming.livestreamapp.services.FollowService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @Autowired
    StreamRepository streamRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> followUser(@RequestBody FollowRequest followRequest) {

        followService.followUser(followRequest.getFollowerId(), followRequest.getFollowingId());

        return ResponseEntity.ok( new MessageResponse("Follow thanh cong"));
    }

}
