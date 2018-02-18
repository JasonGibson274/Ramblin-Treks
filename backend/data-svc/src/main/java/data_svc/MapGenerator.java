package data_svc;

import data_svc.entities.*;

import org.json.JSONArray;
import org.json.JSONObject;
import trek_utils.TrekUtils;

import java.util.*;

public class MapGenerator {
    //TODO move these to parameters
    private final double MIN_LATITUDE;
    private final double MAX_LATITUDE;
    private final double MIN_LONGITUDE;
    private final double MAX_LONGITUDE;
    private final double VOXEL_RESOLUTION;
    private final double SEPARATION_DIST;
    private final double MIN_PATH_LENGTH;

    public MapGenerator(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude, double separationDistance, double voxelResolution, double maxPathLength) {
        this.MIN_LATITUDE = minLatitude;
        this.MAX_LATITUDE = maxLatitude;
        this.MIN_LONGITUDE = minLongitude;
        this.MAX_LONGITUDE = maxLongitude;
        // TODO convert from lat long to meters
        this.SEPARATION_DIST = separationDistance;
        this.VOXEL_RESOLUTION = voxelResolution;
        this.MIN_PATH_LENGTH = maxPathLength;
        // assert that sep > res
    }

    public String generateMap(List<PathLocation> pathLocations, List<BusStop> busStops, BusStopRepository busStopRepository) {
        List<PathLocation> reduced = voxelGrid(pathLocations);
        Map<PathLocation, List<UUID>> graph = findNeighbors(reduced, busStops);
        Map<BusStop, List<UUID>> busGraph = findNeighborsBuses(graph, busStops);
        //Map<UUID, List<PathLocation>> graphSimplified = simplifyMap(graph);
        return serializeMap(graph, busGraph, busStopRepository);
    }

    Map<BusStop, List<UUID>> findNeighborsBuses(Map<PathLocation, List<UUID>> graph, List<BusStop> busStops) {
        Map<BusStop, List<UUID>> result = new HashMap<>();
        for(BusStop busStop : busStops) {
            for(PathLocation pathLocation : graph.keySet()) {
                if(TrekUtils.getDistanceInMetersHaversine(busStop.getLatitude(), busStop.getLongitude(), pathLocation.getLatitude(),  pathLocation.getLongitude()) < SEPARATION_DIST) {
                    result.computeIfAbsent(busStop, k -> new ArrayList<>());
                    result.get(busStop).add(pathLocation.getId());
                }
            }
        }
        return result;
    }

    /**
     * <= and not >= to make sure that no one point is in two grids
     * @param pathLocations
     * @return
     */
    List<PathLocation> voxelGrid(List<PathLocation> pathLocations) {
        boolean[][] voxelGrid = new boolean[(int) Math.round((MAX_LONGITUDE - MIN_LONGITUDE) / VOXEL_RESOLUTION) * 2]
                [(int) Math.round((MAX_LATITUDE - MIN_LATITUDE)/ VOXEL_RESOLUTION) * 2];
        List<PathLocation> result = new ArrayList<>(pathLocations.size());
        for(PathLocation current : pathLocations) {
            if(current.getLatitude() >= MIN_LATITUDE && current.getLatitude() < MAX_LATITUDE &&
                    current.getLongitude() >= MIN_LONGITUDE && current.getLongitude() < MAX_LONGITUDE) {
                if(!voxelGrid[(int) Math.round((current.getLongitude() - MIN_LONGITUDE) / VOXEL_RESOLUTION)]
                [(int)Math.round((current.getLatitude() - MIN_LATITUDE) / VOXEL_RESOLUTION)]) {
                    voxelGrid[(int) Math.round((current.getLongitude() - MIN_LONGITUDE) / VOXEL_RESOLUTION)]
                            [(int)Math.round((current.getLatitude() - MIN_LATITUDE) / VOXEL_RESOLUTION)] = true;
                    result.add(current);
                }
            }
        }
        return result;
    }

    Map<PathLocation, List<UUID>> findNeighbors(List<PathLocation> pathLocations, List<BusStop> busStops) {
        Map<PathLocation, List<UUID>> result = new HashMap<>();
        for(PathLocation location1 : pathLocations) {
            for(PathLocation location2 : pathLocations) {
                if(trek_utils.TrekUtils.getDistanceInMetersHaversine(location1.getLatitude(), location1.getLongitude(),
                        location2.getLatitude(), location2.getLongitude()) < SEPARATION_DIST
                        && !location1.getId().equals(location2.getId())) {
                    result.computeIfAbsent(location1, k -> new ArrayList<>());
                    result.get(location1).add(location2.getId());
                }
            }
            for(BusStop location2 : busStops) {
                if(trek_utils.TrekUtils.getDistanceInMetersHaversine(location1.getLatitude(), location1.getLongitude(),
                        location2.getLatitude(), location2.getLongitude()) < SEPARATION_DIST
                        && !location1.getId().equals(location2.getId())) {
                    result.computeIfAbsent(location1, k -> new ArrayList<>());
                    result.get(location1).add(location2.getUuid());
                }
            }
        }
        return result;
    }

    Map<UUID, List<PathLocation>> simplifyMap(Map<UUID, List<PathLocation>> graph) {
        Map<UUID, List<PathLocation>> result;
        return graph;
    }

    String serializeMap(Map<PathLocation, List<UUID>> graph, Map<BusStop, List<UUID>> busGraph, BusStopRepository busStopRepository) {
        JSONArray result = new JSONArray();
        for(PathLocation current : graph.keySet()) {
            JSONObject locationJson = new JSONObject();
            locationJson.put("type", "path");
            locationJson.put("latitude", current.getLatitude());
            locationJson.put("longitude", current.getLongitude());
            locationJson.put("id", current.getId());
            JSONArray neighbors = new JSONArray();
            for(UUID uuid : graph.get(current)) {
                neighbors.put(uuid);
            }
            locationJson.put("neighbors", neighbors);
            result.put(locationJson);
        }
        for(BusStop current : busGraph.keySet()) {
            JSONObject locationJson = new JSONObject();
            locationJson.put("type", "bus");
            locationJson.put("latitude", current.getLatitude());
            locationJson.put("longitude", current.getLongitude());
            locationJson.put("id", current.getId());
            locationJson.put("color", current.getBusRoute().getRouteColor());
            locationJson.put("name", current.getStopName());
            locationJson.put("uuid", current.getUuid());
            locationJson.put("route", current.getBusRoute().getRouteName());
            JSONArray neighbors = new JSONArray();
            for(UUID uuid : busGraph.get(current)) {
                neighbors.put(uuid);
            }
            locationJson.put("neighbors", neighbors);
            JSONArray busNeighbors = new JSONArray();
            for(BusStop busStop : busStopRepository.findAllByBusRoute(current.getBusRoute())) {
                busNeighbors.put(busStop.getId());
            }
            locationJson.put("busNeighbors", busNeighbors);
            result.put(locationJson);
        }
        return result.toString();
    }
}
