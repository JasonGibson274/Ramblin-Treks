package data_svc.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    private String ketForNextTime;

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

    public String getKetForNextTime() {
        return ketForNextTime;
    }

    public void setKetForNextTime(String ketForNextTime) {
        this.ketForNextTime = ketForNextTime;
    }
}
