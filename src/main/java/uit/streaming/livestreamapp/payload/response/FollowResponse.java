package uit.streaming.livestreamapp.payload.response;

import uit.streaming.livestreamapp.entity.User;

public class FollowResponse {

    private User follower;

    private User following;

    public FollowResponse(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }
}
