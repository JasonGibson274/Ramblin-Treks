package data_svc.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class BusPosition {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String busId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Double speed;

    @NotNull
    private Double heading;

    @NotNull
    private Date time;

    @NotNull
    @Size(min = 1)
    private String busRoute;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> arrivalTimes;

    @NotNull
    private Boolean fullFlag;

    public BusPosition() {
        arrivalTimes = new ArrayList<>();
    }

    public BusPosition(String busId, Double latitude, Double longitude, Double speed, Double heading, String busRoute, List<String> arrivalTimes, Boolean fullFlag) {
        this.busId = busId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
        this.busRoute = busRoute;
        this.arrivalTimes = arrivalTimes;
        this.fullFlag = fullFlag;
    }

    @PrePersist
    public void setup() {
        this.time = new Date();
    }

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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public List<String> getArrivalTimes() {
        return this.arrivalTimes;
    }

    public void setArrivalTimes(List<String> arrivalTimes) {
        this.arrivalTimes = arrivalTimes;
    }

    public Map<Long, Long> getArrivalTimesMap() {
        Map<Long, Long> result = new HashMap<>();
        if(arrivalTimes != null) {
            for(String time : arrivalTimes) {
                String[] line = time.split(",");
                result.put(Long.valueOf(line[0]), Long.valueOf(line[1]));
            }
        }
        return result;
    }


    public Boolean getFullFlag() {
        return fullFlag;
    }

    public void setFullFlag(Boolean fullFlag) {
        this.fullFlag = fullFlag;
    }

    public void addArrivalTime(String s) {
        if(arrivalTimes == null) {
            this.arrivalTimes = new ArrayList<>();
        }
        this.arrivalTimes.add(s);
    }
}
