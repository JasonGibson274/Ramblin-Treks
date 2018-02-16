package data_svc;

import data_svc.entities.*;
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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DataService {

    private final PathLocationRepository pathLocationRepository;
    private final BusPositionRepository busPositionRepository;
    private final BusRouteRepository busRouteRepository;
    private final BusStopRepository busStopRepository;
    private final RestTemplate restTemplate;

    @PostConstruct
    public void setUpScheduler() {
        GtBusesApiCalls.getRoutes(restTemplate, busRouteRepository);
        GtBusesApiCalls.getStops(restTemplate, busStopRepository, busRouteRepository);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        System.out.println("scheduler set up");
        scheduler.scheduleAtFixedRate(new GetBusesTask(this), 0, 3, TimeUnit.MINUTES);
    }

    @Autowired
    public DataService(PathLocationRepository pathLocationRepository, BusPositionRepository busPositionRepository,
                       BusRouteRepository busRouteRepository, BusStopRepository busStopRepository) {
        this.pathLocationRepository = pathLocationRepository;
        this.busPositionRepository = busPositionRepository;
        this.busRouteRepository = busRouteRepository;
        this.busStopRepository = busStopRepository;
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
                -84.3862009, 30, 0.0001, 0.002);
    }

    public List<BusPosition> getAllBusPositions() {
        return busPositionRepository.findAll();
    }

    public void grabAllBusPositions() {
        GtBusesApiCalls.getBusInformation(restTemplate, busPositionRepository, busRouteRepository);
    }
}
