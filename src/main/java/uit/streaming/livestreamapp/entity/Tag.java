package uit.streaming.livestreamapp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String tagName;

    @NotBlank
    @Size(max = 20)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stream_tag",
            joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id", referencedColumnName = "id"))
    private Set<Stream> streams;

    public Tag() {
    }

    public Tag(String tagName, String description, Set<Stream> streams) {
        this.tagName = tagName;
        this.description = description;
        this.streams = streams;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Stream> getStreams() {
        return streams;
    }

    public void setStreams(Set<Stream> streams) {
        this.streams = streams;
    }
}
