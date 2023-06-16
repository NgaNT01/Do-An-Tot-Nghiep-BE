package uit.streaming.livestreamapp.payload.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;

public class StatisticResponse {

    private int totalViews;

    private Long totalDurations;

    private int totalFollowers;

    public StatisticResponse(int totalViews, Long totalDurations, int totalFollowers) {
        this.totalViews = totalViews;
        this.totalDurations = totalDurations;
        this.totalFollowers = totalFollowers;
    }

    public StatisticResponse(int totalViews, Long totalDurations) {
        this.totalViews = totalViews;
        this.totalDurations = totalDurations;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public Long getTotalDurations() {
        return totalDurations;
    }

    public void setTotalDurations(Long totalDurations) {
        this.totalDurations = totalDurations;
    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }
}
