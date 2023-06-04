package uit.streaming.livestreamapp.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class StopStreamRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private Long streamId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String streamName;

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }
}
