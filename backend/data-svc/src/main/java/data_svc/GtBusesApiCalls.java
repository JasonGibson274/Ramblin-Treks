package data_svc;

import data_svc.entities.*;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

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
            parseResponseBusPosition(object.getString("body"), busRouteRepository, busPositionRepository, busStopRepository);
            return true;
        }
        return false;
    }

    static void parseResponseBusPosition(String body, BusRouteRepository busRouteRepository, BusPositionRepository busPositionRepository, BusStopRepository busStopRepository) {
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
