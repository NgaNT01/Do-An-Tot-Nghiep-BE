package uit.streaming.livestreamapp.payload.response;

import uit.streaming.livestreamapp.entity.Category;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

public class StreamResponse {

    @NotBlank
    @Size(min = 3, max = 50)
    private Long streamId;

    @NotBlank
    @Size(min = 3, max = 500)
    private String streamName;

    @Size(max = 500)
    private String description;

    private Set<Category> categories;

    private String thumbnailUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotBlank
    @Size(max = 400)
    private String status;

    private Long userId;


    public StreamResponse(Long streamId, String streamName, String description, Set<Category> categories, String thumbnailUrl, LocalDateTime startTime, String status, Long userId) {
        this.streamId = streamId;
        this.streamName = streamName;
        this.description = description;
        this.categories = categories;
        this.thumbnailUrl = thumbnailUrl;
        this.startTime = startTime;
        this.status = status;
        this.userId = userId;
    }

    public StreamResponse(Long streamId, String streamName, String description, Set<Category> categories, String thumbnailUrl, LocalDateTime startTime, LocalDateTime endTime, String status, Long userId) {
        this.streamId = streamId;
        this.streamName = streamName;
        this.description = description;
        this.categories = categories;
        this.thumbnailUrl = thumbnailUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.userId = userId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
