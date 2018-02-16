package pathing_svc;

//import utilities.trek_utils.TrekUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/pathing")
@CrossOrigin
public class PathingController {

    private final PathingService pathingService;

    @Autowired
    public PathingController(PathingService pathingService) {
        this.pathingService = pathingService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getPath(@RequestParam(value = "cookie", required = false) UUID id, @RequestBody String request) {
        return pathingService.getPath(id, new JSONObject(request));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/csv")
    public void downloadCSVSimple(@RequestParam(value = "cookie", required = false) UUID id, HttpServletResponse response,
                                  @RequestParam(value = "startLat") double startLat, @RequestParam(value = "startLon") double startLon,
                                  @RequestParam(value = "endLat") double endtLat, @RequestParam(value = "endLon") double endLon) {
        pathingService.getPathCsv(id, response, startLat, startLon, endtLat, endLon);
    }
}
