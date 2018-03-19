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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

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
        //ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //System.out.println("scheduler set up");
        //System.out.println(busStopRepository.count());
        //scheduler.scheduleAtFixedRate(new GetBusesTask(this), 0, 10, TimeUnit.SECONDS);
    }

    @Autowired
    public DataService(PathLocationRepository pathLocationRepository, BusPositionRepository busPositionRepository,
                       BusRouteRepository busRouteRepository, BusStopRepository busStopRepository, RestTemplate restTemplate) {
        this.pathLocationRepository = pathLocationRepository;
        this.busPositionRepository = busPositionRepository;
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

    public List<BusPosition> getAllBusPositions() {
        return busPositionRepository.findAll();
    }

    public void grabAllBusPositions() {
        System.out.println("got all bus positions");
        GtBusesApiCalls.getBusInformation(restTemplate, busPositionRepository, busRouteRepository, busStopRepository);
    }

    public void createBusCsv(HttpServletResponse response) {
        String csvFileName = "idk.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter;
        try {
            csvWriter = new CsvBeanWriter(response.getWriter(),
                    CsvPreference.STANDARD_PREFERENCE);

            String[] header = {"latitude", "longitude", "speed", "heading", "time", "route"};
            List<String> headers = Arrays.stream(header).collect(toList());

            List<BusPosition> list = busPositionRepository.findAllByFullFlag(true);
            if(list.size() == 0) {
                return;
            }
            /*for(int i = 0; i < list.get(0).getArrivalTimes().size(); i++) {
                headers.add(String.valueOf(i) + " stop");
                headers.add(String.valueOf(i) + " stop time");
            }

            csvWriter.writeHeader(header);

            for(BusPosition cur : list) {
                StringBuilder result = new StringBuilder();
                result.append(cur.getLatitude());
                result.append(",");
                result.append(cur.getLongitude());
                result.append(",");
                result.append(cur.getSpeed());
                result.append(",");
                result.append(cur.getHeading());
                result.append(",");
                result.append(cur.getTime());
                result.append(",");
                result.append(cur.getBusRoute());
                result.append(",");
                for(int i = 0; i < cur.getArrivalTimes().size(); i++) {
                    result.append(cur.getArrivalTimes().get(i).split(",")[0]);
                    result.append(",");
                    result.append(cur.getArrivalTimes().get(i).split(",")[1]);
                    result.append(",");
                }
                String temp = result.toString().substring(0, result.toString().length() - 1);
                csvWriter.write(temp, header);
            }*/

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
