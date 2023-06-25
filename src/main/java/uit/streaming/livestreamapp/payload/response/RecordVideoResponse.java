package uit.streaming.livestreamapp.payload.response;

import java.time.LocalDateTime;

public class RecordVideoResponse {

    private String recordUrl;

    public String recordName;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    private String thumbnailUrl;

    public Long streamId;

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public RecordVideoResponse(String recordUrl, String recordName, LocalDateTime startTime, LocalDateTime endTime, String thumbnailUrl, Long streamId) {
        this.recordUrl = recordUrl;
        this.recordName = recordName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.thumbnailUrl = thumbnailUrl;
        this.streamId = streamId;
    }
}
