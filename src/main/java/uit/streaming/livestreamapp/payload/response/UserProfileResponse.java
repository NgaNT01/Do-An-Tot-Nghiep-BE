package uit.streaming.livestreamapp.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uit.streaming.livestreamapp.entity.Category;
import uit.streaming.livestreamapp.entity.Role;
import uit.streaming.livestreamapp.entity.Stream;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserProfileResponse {

    @NotBlank
    @Size(min = 3, max = 50)
    private Long userId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Size(max = 50)
    private String email;

    @Size(max = 500)
    private String avatarUrl;

    private Boolean isLive;

    private Set<Role> roles;

    private int followerCount;

    @JsonIgnore
    private Set<Stream> streams;

    public UserProfileResponse(Long userId, String username, String email, String avatarUrl, Boolean isLive,int followerCount, Set<Role> roles, Set<Stream> streams) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.isLive = isLive;
        this.followerCount = followerCount;
        this.roles = roles;
        this.streams = streams;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Stream> getStreams() {
        return streams;
    }

    public void setStreams(Set<Stream> streams) {
        this.streams = streams;
    }
}
