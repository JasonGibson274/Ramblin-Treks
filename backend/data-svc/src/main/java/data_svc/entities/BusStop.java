package data_svc.entities;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name="BusStop")
public class BusStop {
    @NotNull
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Size(min = 1)
    private String stopName;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    @Size(min = 1)
    private String busRoute;

    public BusStop() {}

    public BusStop(String stopName, double latitude, double longitude) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }
}
