package pathing_svc;

import pathing_svc.entities.SearchLocation;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private List<SearchLocation> locations;
    private double cost;
    private double heuristic;

    public Path() {
        this(new ArrayList<>(), 0);
    }

    public Path(List<SearchLocation> locations, double cost) {
        this.locations = locations;
        this.cost = cost;
        this.heuristic = 0;
    }

    public List<SearchLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<SearchLocation> locations) {
        this.locations = locations;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }
}
