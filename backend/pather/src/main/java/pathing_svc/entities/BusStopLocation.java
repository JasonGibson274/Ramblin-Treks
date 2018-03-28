package pathing_svc.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class BusStopLocation extends SearchLocation {

    @NotNull
    @Size(min = 1)
    private String color;

    @NotNull
    @Size(min = 1)
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(min = 1)
    @Column(name = "route")
    private String route;

    @ElementCollection
    private List<Long> arrivalTimes;

    public BusStopLocation() {
    }

    public BusStopLocation(UUID id, double latitude, double longitude, Set<UUID> neighbors, String color,
                           String name, String route) {
        super(id, latitude, longitude, neighbors);
        this.color = color;
        this.name = name;
        this.route = route;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<Long> getArrivalTimes() {
        return arrivalTimes;
    }

    public void setArrivalTimes(List<Long> arrivalTimes) {
        this.arrivalTimes = arrivalTimes;
    }
}
