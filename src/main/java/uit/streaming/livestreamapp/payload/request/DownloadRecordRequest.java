package uit.streaming.livestreamapp.payload.request;

import javax.validation.constraints.NotBlank;

public class DownloadRecordRequest {

    @NotBlank
    private String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
