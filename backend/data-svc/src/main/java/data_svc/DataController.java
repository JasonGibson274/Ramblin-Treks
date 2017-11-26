package data_svc;

import data_svc.entities.PathLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
