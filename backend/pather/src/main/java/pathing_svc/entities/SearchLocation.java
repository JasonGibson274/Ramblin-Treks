package pathing_svc.entities;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity
public class SearchLocation {
    @Id
    @NotNull
    private UUID id;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @ElementCollection
    private Set<UUID> neighbors;

    public SearchLocation() {
    }

    public SearchLocation(UUID id, double latitude, double longitude, Set<UUID> neighbors) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.neighbors = neighbors;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Set<UUID> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<UUID> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchLocation that = (SearchLocation) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(id) + "," + String.valueOf(latitude) + "," + String.valueOf(longitude);
    }
}
