package data_svc;

import data_svc.entities.*;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

public class GtBusesApiCalls {
    private static String getString(String value, String line) {
        return line.substring(line.indexOf(value) + value.length() + 2, line.indexOf("\"", line.indexOf(value) + value.length() + 2));
    }

    public static boolean getStopsAndRoutes(RestTemplate restTemplate, BusRouteRepository busRouteRepository,
                                            BusStopRepository busStopRepository) {

        String url = "https://gtbuses.herokuapp.com/agencies/georgia-tech/routes";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if(response == null) {
            return false;
        }

        if (HttpStatus.OK == response.getStatusCode()) {
            JSONObject object = new JSONObject(response);
            parseResponseRouteAndStop(object.getString("body"), busRouteRepository, busRouteRepository, busStopRepository);
            return true;
        }
        return false;

    }

    public static void parseResponseRouteAndStop(String body, BusRouteRepository busRouteRepository,
                                                 BusRouteRepository routeRepository, BusStopRepository busStopRepository) {
        String[] lines = body.split("\n");
        String busRoute = null;
        for(String line : lines) {
            if(line.contains("route tag=")) {
                BusRoute busRouteObject = new BusRoute();
                busRoute = getString("tag", line);
                busRouteObject.setRouteColor(getString("color", line));
                busRouteObject.setId(busRoute);
                busRouteRepository.save(busRouteObject);
            } else if(line.contains("stop tag=") && line.contains("title=")) {
                BusStop busStop = new BusStop();
                busStop.setBusRoute(busRoute);
                busStop.setStopName(getString("tag", line));
                busStop.setLatitude(Double.parseDouble(getString("lat", line)));
                busStop.setLongitude(Double.parseDouble(getString("lon", line)));
                busStopRepository.save(busStop);
            }
        }
    }


}
