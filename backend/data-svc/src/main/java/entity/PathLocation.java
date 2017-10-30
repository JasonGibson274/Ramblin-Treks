package entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class PathLocation {
    @Id
    private UUID id;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public PathLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
