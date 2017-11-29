package data_svc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class BusPosition {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String busId;

    @NotNull
    private String route;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Double pLatitude;

    @NotNull
    private Double pLongitude;

    @NotNull
    private Double speed;

    @NotNull
    private String jobId;

    @NotNull
    private String ts;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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

    public Double getpLatitude() {
        return pLatitude;
    }

    public void setpLatitude(Double pLatitude) {
        this.pLatitude = pLatitude;
    }

    public Double getpLongitude() {
        return pLongitude;
    }

    public void setpLongitude(Double pLongitude) {
        this.pLongitude = pLongitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
