package pathing_svc;

import pathing_svc.entities.BusStopLocation;
import pathing_svc.entities.SearchLocation;
import trek_utils.TrekUtils;

import java.util.*;

public class StateComparator implements Comparator<Path> {

    private final double walkingSpeed = 1.4; //in m/s

    private final SearchLocation goal;

    public StateComparator(SearchLocation goal) {
        this.goal = goal;
    }

    @Override
    public int compare(Path path1, Path path2) {
        return Double.compare(path1.getCost() + path1.getHeuristic(), path2.getCost() + path2.getHeuristic());
    }

    double getDistanceCost(Path path, SearchLocation searchLocation) {
        if(searchLocation != null) {
            double result = 0;
            SearchLocation start = path.getLocations().get(path.getLocations().size() - 1);
            boolean success = false;
            if(searchLocation instanceof BusStopLocation && start instanceof BusStopLocation) {
                BusStopLocation busStopLocation = (BusStopLocation) searchLocation;
                BusStopLocation startBus = (BusStopLocation) start;
                List<Long> sorted = startBus.getArrivalTimes();
                Collections.sort(sorted);

                for(Long arrivalTime : sorted) {
                    if(arrivalTime > path.getCost()) {
                        result += arrivalTime;
                        break;
                    }
                }
                if(result > 0) {
                    sorted = busStopLocation.getArrivalTimes();
                    Collections.sort(sorted);
                    for(Long arrivalTime : sorted) {
                        if(arrivalTime > result) {
                            result = arrivalTime + arrivalTime / path.getCost();
                            success = true;
                            break;
                        }
                    }
                }
            }
            if(!success) {
                result += path.getCost();
                result += TrekUtils.getDistanceInMetersHaversine(start.getLatitude(), start.getLongitude(),
                        searchLocation.getLatitude(), searchLocation.getLongitude()) / walkingSpeed;
            }
            return result;
        }
        return 0;
    }

    double getHeuristicCost(SearchLocation location) {
        return TrekUtils.getDistanceInMetersHaversine(location.getLatitude(), location.getLongitude(), goal.getLatitude(), goal.getLongitude()) / walkingSpeed;
    }
}
