package uit.streaming.livestreamapp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateCategoryRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 50)
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
