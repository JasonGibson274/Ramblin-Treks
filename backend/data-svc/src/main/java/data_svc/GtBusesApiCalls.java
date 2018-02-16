package data_svc;

import data_svc.entities.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class GtBusesApiCalls {

    public static boolean getBusInformation(RestTemplate restTemplate, BusPositionRepository busPositionRepository, BusRouteRepository busRouteRepository) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://m.gatech.edu:80/api/buses/position",
                String.class);

        if (HttpStatus.OK == response.getStatusCode()) {

            JSONObject object = new JSONObject(response);
            JSONArray body = new JSONArray(object.getString("body"));
            for(int i = 0; i < body.length(); i++) {
                BusPosition busPosition = new BusPosition();
                busPosition.setBusId(body.getJSONObject(i).getString("id"));
                String busRouteId = body.getJSONObject(i).getString("route");
                busPosition.setBusRoute(busRouteRepository.findOne(busRouteId));
                busPosition.setLatitude(new Double(body.getJSONObject(i).get("lat").toString()));
                busPosition.setLongitude(new Double(body.getJSONObject(i).get("lng").toString()));
                busPosition.setpLatitude(new Double(body.getJSONObject(i).get("plat").toString()));
                busPosition.setpLongitude(new Double(body.getJSONObject(i).get("plng").toString()));
                busPosition.setSpeed(new Double(body.getJSONObject(i).get("speed").toString()));
                busPosition.setJobId(body.getJSONObject(i).getString("jobID"));
                busPosition.setTs(body.getJSONObject(i).getString("ts"));
                busPositionRepository.save(busPosition);
            }
            return true;
        }
        return false;
    }

    public static boolean getRoutes(RestTemplate restTemplate, BusRouteRepository busRouteRepository) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://m.gatech.edu:80/api/buses/route",
                String.class);

        if (HttpStatus.OK == response.getStatusCode()) {

            JSONObject object = new JSONObject(response);
            JSONArray body = new JSONArray(object.getString("body"));
            for(int i = 0; i < body.length(); i++) {
                BusRoute busRoute = new BusRoute();
                busRoute.setId(body.getJSONObject(i).getString("route_id"));
                busRoute.setRouteName(body.getJSONObject(i).getString("route_actual_name"));
                busRoute.setRouteColor(body.getJSONObject(i).getString("route_color"));
                //busRoute.setKetForNextTime(body.getJSONObject(i).getString("keyForNextTime"));
                busRouteRepository.save(busRoute);
            }
            return true;
        }
        return false;
    }

    public static boolean getStops(RestTemplate restTemplate, BusStopRepository busStopRepository, BusRouteRepository busRouteRepository) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://m.gatech.edu:80/api/buses/stop",
                String.class);

        if (HttpStatus.OK == response.getStatusCode()) {

            JSONObject object = new JSONObject(response);
            JSONArray body = new JSONArray(object.getString("body"));
            for(int i = 0; i < body.length(); i++) {
                BusStop busStop = new BusStop();
                String routeId = body.getJSONObject(i).getString("route_id");
                busStop.setBusRoute(busRouteRepository.findOne(routeId));
                busStop.setId(Long.valueOf(body.getJSONObject(i).getString("reference_stop_id")));
                busStop.setStopName(body.getJSONObject(i).getString("stop_name"));
                busStop.setDirection(body.getJSONObject(i).getString("trip_id"));
                busStop.setLatitude(Double.parseDouble(body.getJSONObject(i).getString("stop_lat")));
                busStop.setLongitude(Double.parseDouble(body.getJSONObject(i).getString("stop_lon")));
                busStopRepository.save(busStop);
            }
            return true;
        }
        return false;
    }
}
