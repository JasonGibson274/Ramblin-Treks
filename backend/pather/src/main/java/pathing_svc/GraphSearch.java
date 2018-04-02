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

    Path aStar(SearchLocationRepository searchLocationRepository, BusStopLocationRepository busStopLocationRepository) {
        StateComparator comparator = new StateComparator(goal);
        PriorityQueue<Path> frontier = new PriorityQueue<>(Math.toIntExact(searchLocationRepository.count()), comparator);
        Set<SearchLocation> expanded = new HashSet<>(Math.toIntExact(searchLocationRepository.count()) + Math.toIntExact(busStopLocationRepository.count()));
        Path path = new Path();
        path.setLocations(Collections.singletonList(start));
        frontier.add(path);
        while(!frontier.isEmpty()) {
            Path current = frontier.poll();
            SearchLocation last = current.getLocations().get(current.getLocations().size() - 1);
            if(isGoal(last, goal)) {
                return current;
            }
            if(!expanded.contains(last)) {
                expanded.add(last);
                for(SearchLocation location : generateSuccessors(last, searchLocationRepository,
                        busStopLocationRepository)) {
                    Path tempPath = new Path();
                    List<SearchLocation> temp = new ArrayList<>(current.getLocations());
                    temp.add(location);
                    tempPath.setLocations(temp);
                    tempPath.setCost(comparator.getDistanceCost(current, location));
                    tempPath.setHeuristic(comparator.getHeuristicCost(location));
                    frontier.add(tempPath);
                }
            }
        }
        throw new NoPathException("");
    }

    Set<SearchLocation> generateSuccessors(SearchLocation location, SearchLocationRepository searchLocationRepository,
                                           BusStopLocationRepository busStopLocationRepository) {
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

    private boolean isGoal(SearchLocation current, SearchLocation goal) {
        return current.equals(goal);
    }
}