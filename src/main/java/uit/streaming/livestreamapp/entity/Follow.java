package uit.streaming.livestreamapp.entity;

import javax.persistence.*;

@Entity
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User follower;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "following_id", referencedColumnName = "id")
    private User following;

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public Follow() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
