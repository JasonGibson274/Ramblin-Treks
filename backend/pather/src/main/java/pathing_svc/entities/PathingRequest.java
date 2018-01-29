package pathing_svc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class PathingRequest {
    @NotNull
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private double startLatitude;

    @NotNull
    private double endLatitude;

    @NotNull
    private double startLongitude;

    @NotNull
    private double endLongitude;

    public PathingRequest() {
    }



    public PathingRequest(double startLatitude, double endLatitude, double startLongitude, double endLongitude) {
        this.startLatitude = startLatitude;
        this.endLatitude = endLatitude;
        this.startLongitude = startLongitude;
        this.endLongitude = endLongitude;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathingRequest that = (PathingRequest) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}

