package data_svc;

import data_svc.entities.BusPosition;
import data_svc.entities.BusPositionRepository;
import data_svc.entities.PathLocation;
import data_svc.entities.PathLocationRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import trek_utils.TrekUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DataService {

    private final PathLocationRepository pathLocationRepository;
    private final BusPositionRepository busPositionRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public DataService(PathLocationRepository pathLocationRepository, BusPositionRepository busPositionRepository) {
        this.pathLocationRepository = pathLocationRepository;
        this.busPositionRepository = busPositionRepository;
        this.restTemplate = new RestTemplate();
    }

    public void saveLocation(double latitude, double longitude) {
        final PathLocation path = new PathLocation(latitude, longitude);
        pathLocationRepository.save(path);
    }

    public List<PathLocation> findAllPathLocations() {
        return pathLocationRepository.findAll();
    }

    public void createCSV(HttpServletResponse response) throws IOException {
        TrekUtils.createCsv(response, new ArrayList<>(pathLocationRepository.findAll()));
    }

    public void createSimplifiedCsv(HttpServletResponse response) throws IOException {
        MapGenerator mapGenerator = createGenerator();
        List<PathLocation> locations = mapGenerator.voxelGrid(pathLocationRepository.findAll());
        TrekUtils.createCsv(response, new ArrayList<>(locations));
    }

    public void saveCsv(String csv) {
        csv = csv.replaceFirst(".*\n", "");
        for(String line : csv.split("\n")) {
            String[] temp = line.split(",");
            saveLocation(new Double(temp[1]), new Double(temp[2]));
        }
    }

    public List<BusPosition> getAllBusPositions() {
        return busPositionRepository.findAll();
    }

    public List<BusPosition> getBusInformation() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://m.gatech.edu/api/buses/position",
                String.class);

        if (HttpStatus.OK == response.getStatusCode()) {

            JSONObject object = new JSONObject(response);
            JSONArray body = new JSONArray(object.getString("body"));
            for(int i = 0; i < body.length(); i++) {
                BusPosition busPosition = new BusPosition();
                busPosition.setBusId(body.getJSONObject(i).getString("id"));
                //busPosition.setRoute(body.getJSONObject(i).getString("route"));
                busPosition.setRoute("null");
                busPosition.setLatitude(new Double(body.getJSONObject(i).get("lat").toString()));
                busPosition.setLongitude(new Double(body.getJSONObject(i).get("lng").toString()));
                busPosition.setpLatitude(new Double(body.getJSONObject(i).get("plat").toString()));
                busPosition.setpLongitude(new Double(body.getJSONObject(i).get("plng").toString()));
                busPosition.setSpeed(new Double(body.getJSONObject(i).get("speed").toString()));
                busPosition.setJobId(body.getJSONObject(i).getString("jobID"));
                busPosition.setTs(body.getJSONObject(i).getString("ts"));
                busPositionRepository.save(busPosition);
            }

            return busPositionRepository.findAll();
        } else {
            return null;
        }
    }

    public long getCount() {
        return pathLocationRepository.count();
    }

    public void deleteLocation(UUID id) {
        pathLocationRepository.delete(id);
    }

    public String getSimpleGraph() {
        MapGenerator mapGenerator = createGenerator();
        return mapGenerator.generateMap(findAllPathLocations());
    }

    private MapGenerator createGenerator() {
        return new MapGenerator(33.7689984,33.7866378,-84.4104695,
                -84.3862009, 15, 0.0001, 0.002);
    }
}
