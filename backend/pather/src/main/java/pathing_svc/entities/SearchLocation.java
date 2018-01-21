package pathing_svc.entities;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class SearchLocation {
    @Id
    @NotNull
    private UUID id;

    @NotNull
    private double latitiude;

    @NotNull
    private double longitude;

    @ElementCollection
    private Set<UUID> neighbors;

    public SearchLocation() {
    }

    public SearchLocation(UUID id, double latitiude, double longitude, Set<UUID> neighbors) {
        this.id = id;
        this.latitiude = latitiude;
        this.longitude = longitude;
        this.neighbors = neighbors;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getLatitiude() {
        return latitiude;
    }

    public void setLatitiude(double latitiude) {
        this.latitiude = latitiude;
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
}
