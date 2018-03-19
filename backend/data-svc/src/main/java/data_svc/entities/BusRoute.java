package data_svc.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

//"keyForNextTime":"1795672596723","displayOn":"1","orderNumber":"1"}

@Entity
public class BusRoute {

    @NotNull
    @Size(min = 1)
    @Id
    private String id;

    @NotNull
    @Size(min = 1)
    private String routeName;

    @NotNull
    @Size(min = 1)
    private String routeColor;

    private String keyForNextTime;

    public BusRoute() {}

    public BusRoute(String id, String routeName, String routeColor) {
        this.id = id;
        this.routeName = routeName;
        this.routeColor = routeColor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public void setRouteColor(String routeColor) {
        this.routeColor = routeColor;
    }

    public String getKeyForNextTime() {
        return keyForNextTime;
    }

    public void setKeyForNextTime(String keyForNextTime) {
        this.keyForNextTime = keyForNextTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusRoute busRoute = (BusRoute) o;
        return Objects.equals(id, busRoute.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
