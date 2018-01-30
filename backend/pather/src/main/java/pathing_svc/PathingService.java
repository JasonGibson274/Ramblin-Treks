package pathing_svc;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pathing_svc.entities.PathingRequest;
import pathing_svc.entities.PathingRequestRepository;
import pathing_svc.entities.SearchLocation;
import pathing_svc.entities.SearchLocationRepository;
import trek_utils.TrekUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class PathingService {

    private final PathingRequestRepository pathingRequestRepository;
    private final SearchLocationRepository searchLocationRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public PathingService(PathingRequestRepository pathingRequestRepository, SearchLocationRepository searchLocationRepository) {
        this.pathingRequestRepository = pathingRequestRepository;
        this.searchLocationRepository = searchLocationRepository;
        this.restTemplate = new RestTemplate();
    }

    String getPath(UUID id, JSONObject request) {
        pullGraphIfAbsent();
        PathingRequest pathingRequest = findOrCreate(id, request);
        SearchLocation[] startAndEnd = findStartAndEndLocation(pathingRequest);
        GraphSearch search = new GraphSearch(startAndEnd[0], startAndEnd[1]);
        List<SearchLocation> path = search.aStar(searchLocationRepository);
        return serializePath(path);
    }

    void pullGraphIfAbsent() {
        if(searchLocationRepository.count() == 0) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://jasongibson274.hopto.org:9001/data/simplified/path",
                    String.class);

            if (HttpStatus.OK == response.getStatusCode()) {
                JSONObject object = new JSONObject(response);
                JSONArray body = new JSONArray(object.getString("body"));
                for(int i = 0; i < body.length(); i++) {
                    JSONObject current = body.getJSONObject(i);
                    SearchLocation newLocation = new SearchLocation();
                    newLocation.setLatitude(current.getDouble("latitude"));
                    newLocation.setLongitude(current.getDouble("longitude"));
                    newLocation.setId(UUID.fromString(current.getString("id")));
                    JSONArray neighbors = current.getJSONArray("neighbors");
                    HashSet<UUID> neighborList = new HashSet<>();
                    for(int j = 0; j < neighbors.length(); j++) {
                        neighborList.add(UUID.fromString(neighbors.getString(j)));
                    }
                    newLocation.setNeighbors(neighborList);
                    searchLocationRepository.save(newLocation);
                }
            }
        }
    }

    String serializePath(List<SearchLocation> path) {
        JSONObject jsonObject = new JSONObject();

        for(int i = 0; i < path.size(); i++) {
            JSONObject node = new JSONObject();
            node.put("latitude", path.get(i).getLatitude());
            node.put("longitude", path.get(i).getLongitude());
            jsonObject.put(String.valueOf(i), node);
        }
        jsonObject.put("orientation", TrekUtils.getBearing(path.get(0).getLatitude(), path.get(0).getLongitude(),
                path.get(path.size() - 1).getLatitude(), path.get(path.size() - 1).getLongitude()));
        return jsonObject.toString();
    }

    SearchLocation[] findStartAndEndLocation(PathingRequest pathingRequest) {
        SearchLocation[] result = new SearchLocation[2];
        double[] resultDist = new double[2];
        for(SearchLocation current: searchLocationRepository.findAll()) {
            double distToStart = Math.sqrt(Math.pow(current.getLatitude() - pathingRequest.getStartLatitude(), 2) +
                    Math.pow(current.getLongitude() - pathingRequest.getStartLongitude(), 2));
            double distToEnd = Math.sqrt(Math.pow(current.getLatitude() - pathingRequest.getEndLatitude(), 2) +
                    Math.pow(current.getLongitude() - pathingRequest.getEndLongitude(), 2));
            if(result[0] == null || resultDist[0] > distToStart) {
                result[0] = current;
                resultDist[0] = distToStart;
            }
            if(result[1] == null || resultDist[1] > distToEnd) {
                result[1] = current;
                resultDist[1] = distToEnd;
            }
        }
        return result;
    }

    PathingRequest findOrCreate(UUID id, JSONObject request) {
        if(id == null || pathingRequestRepository.findOne(id) == null) {
            PathingRequest pathingRequest = new PathingRequest();
            pathingRequest.setStartLatitude(request.getDouble("startLatitude"));
            pathingRequest.setStartLongitude(request.getDouble("startLongitude"));
            pathingRequest.setEndLatitude(request.getDouble("endLatitude"));
            pathingRequest.setEndLongitude(request.getDouble("endLongitude"));
            pathingRequestRepository.save(pathingRequest);
            return pathingRequest;
        } else {
            PathingRequest pathingRequest = pathingRequestRepository.findOne(id);
            if(pathingRequest.getEndLatitude() != request.getDouble("endLatitude") ||
                    pathingRequest.getEndLongitude() != request.getDouble("endLongitude")) {
                pathingRequest.setEndLongitude(request.getDouble("endLongitude"));
                pathingRequest.setEndLatitude(request.getDouble("endLatitude"));
                pathingRequestRepository.save(pathingRequest);
            }
            return pathingRequest;
        }
    }

    public void getPathCsv(UUID id, HttpServletResponse response, double startLat, double startLon, double endtLat, double endLon) {
        pullGraphIfAbsent();
        PathingRequest pathingRequest = new PathingRequest();
        pathingRequest.setStartLatitude(startLat);
        pathingRequest.setStartLongitude(startLon);
        pathingRequest.setEndLatitude(endtLat);
        pathingRequest.setEndLongitude(endLon);
        SearchLocation[] startAndEnd = findStartAndEndLocation(pathingRequest);
        GraphSearch search = new GraphSearch(startAndEnd[0], startAndEnd[1]);
        List<SearchLocation> path = search.aStar(searchLocationRepository);
        try {
            TrekUtils.createCsv(response, new ArrayList<>(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
