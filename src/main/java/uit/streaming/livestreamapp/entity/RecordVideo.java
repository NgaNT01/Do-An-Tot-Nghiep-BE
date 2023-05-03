package uit.streaming.livestreamapp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "record_video")
public class RecordVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String recordUrl;

    @NotBlank
    @Size(max = 20)
    private String recordName;

    @NotBlank
    @Size(max = 20)
    private LocalDateTime startTime;

    @NotBlank
    @Size(max = 20)
    private LocalDateTime endTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stream_id", referencedColumnName = "id")
    private Stream stream;

    public RecordVideo() {
    }

    public RecordVideo(String recordUrl, String recordName, LocalDateTime startTime, LocalDateTime endTime, Stream stream) {
        this.recordUrl = recordUrl;
        this.recordName = recordName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stream = stream;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
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

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}
