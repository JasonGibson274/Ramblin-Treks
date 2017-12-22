package data_svc;

import data_svc.entities.BusPosition;
import data_svc.entities.PathLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/data")
@CrossOrigin
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void sendLocation(@RequestParam double latitude, @RequestParam double longitude) {
        dataService.saveLocation(latitude, longitude);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/path")
    public List<PathLocation> getLocations() {
        return dataService.findAllPathLocations();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/csv")
    public void downloadCSV(HttpServletResponse response) throws IOException {
        dataService.createCSV(response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void uploadCSV(@RequestBody String csv) {
        dataService.saveCsv(csv);
    }

    @RequestMapping(method = RequestMethod.GET, value = "buses")
    public List<BusPosition> getBusesInformation() {
        return dataService.getBusInformation();
    }

    @RequestMapping(method = RequestMethod.GET, value = "count")
    public long getCount() {
        return dataService.getCount();
    }
}