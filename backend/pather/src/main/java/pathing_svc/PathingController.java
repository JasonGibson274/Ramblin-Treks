package pathing_svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public String getPath() {
        return pathingService.getPath();
    }
}
