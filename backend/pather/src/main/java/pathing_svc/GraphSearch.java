package pathing_svc;

import pathing_svc.entities.BusStopLocation;
import pathing_svc.entities.BusStopLocationRepository;
import pathing_svc.entities.SearchLocation;
import pathing_svc.entities.SearchLocationRepository;

import java.util.*;

public class GraphSearch {
    private final SearchLocation start;
    private final SearchLocation goal;


    GraphSearch(SearchLocation start, SearchLocation goal) {
        this.start = start;
        this.goal = goal;
    }

    List<SearchLocation> aStar(SearchLocationRepository searchLocationRepository, BusStopLocationRepository busStopLocationRepository) {
        StateComparator comparator = new StateComparator(goal);
        PriorityQueue<List<SearchLocation>> frontier = new PriorityQueue<>(Math.toIntExact(searchLocationRepository.count()), comparator);
        Set<SearchLocation> expanded = new HashSet<>(Math.toIntExact(searchLocationRepository.count()));
        frontier.add(Collections.singletonList(start));
        while(!frontier.isEmpty()) {
            List<SearchLocation> current = frontier.poll();
            if(isGoal(current.get(current.size() - 1), goal)) {
                return current;
            }
            if(!expanded.contains(current.get(current.size() - 1))) {
                expanded.add(current.get(current.size() - 1));
                for(SearchLocation location : generateSuccessors(current.get(current.size() - 1), searchLocationRepository, busStopLocationRepository)) {
                    List<SearchLocation> temp = new ArrayList<>(current);
                    temp.add(location);
                    frontier.add(temp);
                }
            }
        }
        throw new NoPathException("");
    }

    Set<SearchLocation> generateSuccessors(SearchLocation location, SearchLocationRepository searchLocationRepository, BusStopLocationRepository busStopLocationRepository) {
        Set<SearchLocation> result = new HashSet<>();
        for(UUID neighborId : location.getNeighbors()) {
            if(searchLocationRepository.findOne(neighborId) != null) {
                result.add(searchLocationRepository.findOne(neighborId));
            }
        }
        if(location instanceof BusStopLocation) {
            BusStopLocation busStopLocation = (BusStopLocation) location;
            result.addAll(busStopLocationRepository.findAllByRoute(busStopLocation.getRoute()));
        }
        return result;
    }

    boolean isGoal(SearchLocation current, SearchLocation goal) {
        return current.equals(goal);
    }
}