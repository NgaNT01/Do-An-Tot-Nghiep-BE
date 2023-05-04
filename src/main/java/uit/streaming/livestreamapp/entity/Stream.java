package uit.streaming.livestreamapp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "stream")
public class Stream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String streamName;

    @NotBlank
    @Size(max = 50)
    private String description;

    @NotBlank
    @Size(max = 20)
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "stream")
    private RecordVideo recordVideo;

    @ManyToMany(mappedBy = "streams")
    private Set<Category> categories;

    public Stream() {
    }

    public Stream(String streamName, String description, User user, RecordVideo recordVideo, Set<Category> categories, String status) {
        this.streamName = streamName;
        this.description = description;
        this.user = user;
        this.recordVideo = recordVideo;
        this.categories = categories;
        this.status = status;
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
