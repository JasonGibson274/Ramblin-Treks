package pathing_svc.entities;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class BusStopLocation extends SearchLocation {

    @NotNull
    private Long longId;

    @NotNull
    @Size(min = 1)
    private String color;

    @NotNull
    @Size(min = 1)
    private String name;

    @ElementCollection
    private Set<Long> busNeighbors;

    @NotNull
    @Size(min = 1)
    private String route;

    public BusStopLocation() {
    }

    public BusStopLocation(UUID id, double latitude, double longitude, Set<UUID> neighbors, Long longId, String color, String name, Set<Long> busNeighbors, String route) {
        super(id, latitude, longitude, neighbors);
        this.longId = longId;
        this.color = color;
        this.name = name;
        this.busNeighbors = busNeighbors;
        this.route = route;
    }

    public Long getLongId() {
        return longId;
    }

    public void setLongId(Long longId) {
        this.longId = longId;
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

    public Set<Long> getBusNeighbors() {
        return busNeighbors;
    }

    public void setBusNeighbors(Set<Long> busNeighbors) {
        this.busNeighbors = busNeighbors;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
