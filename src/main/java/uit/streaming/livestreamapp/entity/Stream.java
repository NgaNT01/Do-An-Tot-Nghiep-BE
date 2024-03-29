package uit.streaming.livestreamapp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stream")
public class Stream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String streamName;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 200)
    private String status;

    private Boolean isPublic;

    private String accessCode;

    private int viewerCount;

    private String thumbnail;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "stream")
    private RecordVideo recordVideo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_stream",
            joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Stream() {
    }

    public Stream(String streamName, String description, String status, LocalDateTime startTime) {
        this.streamName = streamName;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
    }

    public Stream(String streamName, String description, String status, String thumbnail, LocalDateTime startTime) {
        this.streamName = streamName;
        this.description = description;
        this.status = status;
        this.thumbnail = thumbnail;
        this.startTime = startTime;
    }

    public Stream(String streamName, String description, String status, String accessCode, int viewerCount, String thumbnail, LocalDateTime startTime) {
        this.streamName = streamName;
        this.description = description;
        this.status = status;
        this.accessCode = accessCode;
        this.viewerCount = viewerCount;
        this.thumbnail = thumbnail;
        this.startTime = startTime;
    }

    public Stream(String streamName, String description, User user, RecordVideo recordVideo, Set<Category> categories, String status) {
        this.streamName = streamName;
        this.description = description;
        this.user = user;
        this.recordVideo = recordVideo;
        this.categories = categories;
        this.status = status;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public int getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(int viewerCount) {
        this.viewerCount = viewerCount;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public RecordVideo getRecordVideo() {
        return recordVideo;
    }

    public void setRecordVideo(RecordVideo recordVideo) {
        this.recordVideo = recordVideo;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }
}
