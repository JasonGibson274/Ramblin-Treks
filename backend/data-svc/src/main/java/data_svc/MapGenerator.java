package data_svc;

import data_svc.entities.PathLocation;

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

    public String generateMap(List<PathLocation> pathLocations) {
        List<PathLocation> reduced = voxelGrid(pathLocations);
        Map<UUID, List<PathLocation>> graph = findNeighbors(reduced);
        Map<UUID, List<PathLocation>> graphSimplified = simplifyMap(graph);
        return serializeMap(graph);
    }

    /**
     * <= and not >= to make sure that no one point is in two grids
     * @param pathLocations
     * @return
     */
    List<PathLocation> voxelGrid(List<PathLocation> pathLocations) {
        boolean[][] voxelGrid = new boolean[(int) Math.round((MAX_LONGITUDE - MIN_LONGITUDE) / VOXEL_RESOLUTION)]
                [(int) Math.round((MAX_LATITUDE - MIN_LATITUDE)/ VOXEL_RESOLUTION)];
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

    Map<UUID, List<PathLocation>> findNeighbors(List<PathLocation> pathLocations) {
        Map<UUID, List<PathLocation>> result = new HashMap<>();
        for(PathLocation location1 : pathLocations) {
            for(PathLocation location2 : pathLocations) {
                if(Math.sqrt(Math.pow(location1.getLatitude() - location2.getLatitude(), 2) +
                        Math.pow(location1.getLongitude() - location2.getLongitude(), 2)) < SEPARATION_DIST
                        && !location1.getId().equals(location2.getId())) {
                    result.computeIfAbsent(location1.getId(), k -> new ArrayList<>());
                    result.get(location1.getId()).add(location2);
                }
            }
        }
        return result;
    }

    Map<UUID, List<PathLocation>> simplifyMap(Map<UUID, List<PathLocation>> graph) {
        Map<UUID, List<PathLocation>> result;
        return graph;
    }

    String serializeMap(Map<UUID, List<PathLocation>> graph) {
        return "";
    }
}
