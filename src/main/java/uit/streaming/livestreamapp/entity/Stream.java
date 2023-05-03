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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @OneToOne(mappedBy = "stream")
    private RecordVideo recordVideo;

    @ManyToMany(mappedBy = "streams")
    private Set<Tag> tags;

    public Stream() {
    }

    public Stream(String streamName, String description, Room room, RecordVideo recordVideo) {
        this.streamName = streamName;
        this.description = description;
        this.room = room;
        this.recordVideo = recordVideo;
    }

    public Stream(String streamName, String description, Room room, RecordVideo recordVideo, Set<Tag> tags) {
        this.streamName = streamName;
        this.description = description;
        this.room = room;
        this.recordVideo = recordVideo;
        this.tags = tags;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
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

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getDescription() {
        return description;
    }

    public Room getRoom() {
        return room;
    }

    public Long getId() {
        return id;
    }
}
