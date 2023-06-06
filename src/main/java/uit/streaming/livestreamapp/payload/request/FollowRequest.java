package uit.streaming.livestreamapp.payload.request;

import uit.streaming.livestreamapp.entity.User;

import javax.validation.constraints.NotBlank;

public class FollowRequest {

    @NotBlank
    private Long followerId;

    @NotBlank
    private Long followingId;

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }
}
