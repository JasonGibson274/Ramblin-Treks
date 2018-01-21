package pathing_svc;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pathing_svc.entities.PathingRequest;
import pathing_svc.entities.PathingRequestRepository;
import pathing_svc.entities.SearchLocation;
import pathing_svc.entities.SearchLocationRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PathingService {

    private final PathingRequestRepository pathingRequestRepository;
    private final SearchLocationRepository searchLocationRepository;

    public PathingService(PathingRequestRepository pathingRequestRepository, SearchLocationRepository searchLocationRepository) {
        this.pathingRequestRepository = pathingRequestRepository;
        this.searchLocationRepository = searchLocationRepository;
    }

    String getPath(UUID id, JSONObject request) {
        PathingRequest pathingRequest = findOrCreate(id, request);
        SearchLocation[] startAndEnd = findStartAndEndLocation(pathingRequest);
        GraphSearch search = new GraphSearch(startAndEnd[0], startAndEnd[1]);
        List<SearchLocation> path = search.aStar(searchLocationRepository);
        return serializePath(path);
    }

    String serializePath(List<SearchLocation> path) {
        JSONObject jsonObject = new JSONObject();

        for(int i = 0; i < path.size(); i++) {
            JSONObject node = new JSONObject();
            node.put("latitude", path.get(i).getLatitude());
            node.put("longitude", path.get(i).getLongitude());
            jsonObject.put(String.valueOf(i), node);
        }
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
}
