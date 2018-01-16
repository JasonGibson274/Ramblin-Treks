package pathing_svc;

import data_svc.entities.PathLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pathing")
@CrossOrigin
public class PathingController {

    private final PathingService pathingService;

    @Autowired
    public PathingController(PathingService pathingService) {
        this.pathingService = pathingService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PathLocation> getPath() {
        return pathingService.getPath();
    }
}
