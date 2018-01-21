package pathing_svc;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public String getPath(@RequestParam(value = "cookie", required = false) UUID id, @RequestBody JSONObject request) {
        return pathingService.getPath(id, request);
    }
}
