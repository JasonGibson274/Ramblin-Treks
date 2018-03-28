package data_svc;

import data_svc.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class DataService {

    private final PathLocationRepository pathLocationRepository;
    private final BusRouteRepository busRouteRepository;
    private final BusStopRepository busStopRepository;
    private final RestTemplate restTemplate;

    @PostConstruct
    public void setUp() {
        busStopRepository.deleteAll();
        busRouteRepository.deleteAll();
        GtBusesApiCalls.getStopsAndRoutes(restTemplate, busRouteRepository, busStopRepository);
    }

    @Autowired
    public DataService(PathLocationRepository pathLocationRepository,
                       BusRouteRepository busRouteRepository, BusStopRepository busStopRepository, RestTemplate restTemplate) {
        this.pathLocationRepository = pathLocationRepository;
        this.busRouteRepository = busRouteRepository;
        this.busStopRepository = busStopRepository;
        this.restTemplate = restTemplate;
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
        return mapGenerator.generateMap(findAllPathLocations(), busStopRepository.findAll(), busStopRepository, busRouteRepository);
    }

    private MapGenerator createGenerator() {
        return new MapGenerator(33.7689984,33.7866378,-84.4104695,
                -84.3862009, 30, 0.0001, 0.002);
    }

    public List<BusStop> getAllBusStops() {
        return busStopRepository.findAll();
    }
}
