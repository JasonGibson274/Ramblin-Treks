package pathing_svc;

import pathing_svc.entities.SearchLocation;

import java.util.Comparator;
import java.util.List;

public class StateComparator implements Comparator<List<SearchLocation>> {

    private final SearchLocation goal;

    public StateComparator(SearchLocation goal) {
        this.goal = goal;
    }

    @Override
    public int compare(List<SearchLocation> locs1, List<SearchLocation> locs2) {
        return Double.compare(getPathCost(locs1), getPathCost(locs2));
    }

    double getPathCost(List<SearchLocation> list) {
        double result = 0;
        if(list.size() == 0) {
            return 0;
        }
        for(int i = 0; i < list.size() - 1; i++) {
            result += Utilities.distance(list.get(i), list.get(i + 1));
        }
        result += getHeuristicCost(list.get(list.size() - 1));
        return result;
    }

    double getHeuristicCost(SearchLocation location) {
        return Utilities.distance(location, goal);
    }
}
