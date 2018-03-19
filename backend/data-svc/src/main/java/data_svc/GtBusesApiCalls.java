package data_svc;

import data_svc.entities.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import trek_utils.TrekUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GtBusesApiCalls {

    public static boolean getBusInformation(RestTemplate restTemplate, BusPositionRepository busPositionRepository, BusRouteRepository busRouteRepository, BusStopRepository busStopRepository) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "https://gtbuses.herokuapp.com/agencies/georgia-tech/vehicles",
                String.class);

        if(response == null) {
            return false;
        }

        if (HttpStatus.OK == response.getStatusCode()) {
            JSONObject object = new JSONObject(response);
            parseResponse(object.getString("body"), busRouteRepository, busPositionRepository, busStopRepository);
            return true;
        }
        return false;
    }

    static void parseResponse(String body, BusRouteRepository busRouteRepository, BusPositionRepository busPositionRepository, BusStopRepository busStopRepository) {
        String[] lines = body.split("\n");
        for(String line : lines) {
            if(line.contains("vehicle")) {
                BusPosition busPosition = new BusPosition();
                busPosition.setBusId(getString("id", line));
                busPosition.setHeading(new Double(getString("heading", line)));
                BusRoute busRoute = busRouteRepository.findOne(getString("routeTag", line));
                busPosition.setBusRoute(getString("routeTag", line));
                busPosition.setLatitude(new Double(getString("lat", line)));
                busPosition.setLongitude(new Double(getString("lon", line)));
                busPosition.setSpeed(new Double(getString("speedKmHr", line)));
                busPosition.setFullFlag(false);
                busPosition.setTime(new Date());
                //List<BusStop> possibleStops = busStopRepository.findAllByBusRoute(busRoute);
                /*for(BusStop stop : possibleStops) {
                    //System.out.println(TrekUtils.getDistanceInMetersHaversine(stop.getLatitude(), stop.getLongitude(), busPosition.getLatitude(), busPosition.getLongitude()));
                    if(TrekUtils.getDistanceInMetersHaversine(stop.getLatitude(), stop.getLongitude(), busPosition.getLatitude(), busPosition.getLongitude()) < 10) {
                        //System.out.println("less than 20");
                        for(BusPosition currPosition : busPositionRepository.findAllByBusIdAndFullFlag(busPosition.getBusId(), Boolean.FALSE)) {
                            //System.out.println("I got here");
                            if(!currPosition.getArrivalTimesMap().containsKey(stop.getId())) {
                                //System.out.println("I added");
                                currPosition.addArrivalTime(stop.getId() + "," + String.valueOf(busPosition.getTime().getTime() - currPosition.getTime().getTime()));
                                if(possibleStops.size() == currPosition.getArrivalTimes().size()) {
                                    currPosition.setFullFlag(Boolean.TRUE);
                                }
                            }
                            busPositionRepository.save(currPosition);
                        }
                    }
                }*/
                busPositionRepository.save(busPosition);
            }
        }
    }

    private static String getString(String value, String line) {
        return line.substring(line.indexOf(value) + value.length() + 2, line.indexOf("\"", line.indexOf(value) + value.length() + 2));
    }

    public static boolean getRoutes(RestTemplate restTemplate, BusRouteRepository busRouteRepository) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://m.gatech.edu:80/api/buses/route",
                String.class);
        if(response == null) {
            return false;
        }

        if (HttpStatus.OK == response.getStatusCode()) {

            JSONObject object = new JSONObject(response);
            JSONArray body = new JSONArray(object.getString("body"));
            for(int i = 0; i < body.length(); i++) {
                BusRoute busRoute = new BusRoute();
                busRoute.setId(body.getJSONObject(i).getString("route_id"));
                busRoute.setRouteName(body.getJSONObject(i).getString("route_actual_name"));
                busRoute.setRouteColor(body.getJSONObject(i).getString("route_color"));
                //busRoute.setKeyForNextTime(body.getJSONObject(i).getString("keyForNextTime"));
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

        if(response == null) {
            return false;
        }

        if (HttpStatus.OK == response.getStatusCode()) {

            JSONObject object = new JSONObject(response);
            JSONArray body = new JSONArray(object.getString("body"));
            for(int i = 0; i < body.length(); i++) {
                BusStop busStop = new BusStop();
                String routeId = body.getJSONObject(i).getString("route_id");
                busStop.setBusRoute(routeId);
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
