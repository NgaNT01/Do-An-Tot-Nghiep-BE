package uit.streaming.livestreamapp.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class CreateStreamRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String streamName;

    @Size(max = 50)
    @Email
    private String description;

    private Set<String> categories;

    @NotBlank
    @Size(max = 40)
    private String streamType;

    @NotBlank
    @Size(max = 40)
    private String status;

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

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public String getStreamType() {
        return streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}