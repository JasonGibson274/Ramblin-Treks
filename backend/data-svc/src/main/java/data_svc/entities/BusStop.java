package data_svc.entities;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
public class BusStop {
    @NotNull
    @Id
    private Long id;

    @NotNull
    @Size(min = 1)
    private String stopName;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    @Size(min = 1)
    private String direction;

    @ManyToOne
    private BusRoute busRoute;

    @NotNull
    private UUID uuid;

    public BusStop() {}

    public BusStop(Long id, String stopName, double latitude, double longitude, String direction) {
        this.id = id;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
    }

    @PrePersist
    public void setUp() {
        this.uuid = UUID.randomUUID();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BusRoute getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
