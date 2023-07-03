package uit.streaming.livestreamapp.payload.request;

import javax.validation.constraints.NotBlank;

public class ChangeAvatarRequest {

    @NotBlank
    private Long userId;

    @NotBlank
    private String stringAvatar;

    @NotBlank
    private String avatarFileName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStringAvatar() {
        return stringAvatar;
    }

    public void setStringAvatar(String stringAvatar) {
        this.stringAvatar = stringAvatar;
    }

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }
}
