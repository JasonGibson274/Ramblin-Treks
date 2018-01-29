package pathing_svc;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

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
}
