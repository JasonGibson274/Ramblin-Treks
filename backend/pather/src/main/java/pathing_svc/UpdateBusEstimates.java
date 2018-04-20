package pathing_svc;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pathing_svc.entities.BusStopLocation;
import pathing_svc.entities.BusStopLocationRepository;

import java.util.ArrayList;
import java.util.List;

public class UpdateBusEstimates implements Runnable {

    private final RestTemplate restTemplate;
    private final BusStopLocationRepository busStopLocationRepository;
    private boolean updateVal = true;

    public UpdateBusEstimates(RestTemplate restTemplate, BusStopLocationRepository busStopLocationRepository) {
        this.restTemplate = restTemplate;
        this.busStopLocationRepository = busStopLocationRepository;
    }

    @Override
    public void run() {
        updateVal = true;
    }

    public void update() {
        if(updateVal) {
            int stop = 0;
            List<BusStopLocation> stops = busStopLocationRepository.findAllByOrderByRoute();
            String route = stops.get(0).getRoute();
            for(BusStopLocation cur : stops) {
                cur.setArrivalTimes(new ArrayList<>());
                busStopLocationRepository.save(cur);
            }
            do {
                String url = "https://gtbuses.herokuapp.com/agencies/georgia-tech/multiPredictions?stops=";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(url);
                boolean first = true;
                for (;stop < stops.size() && stringBuilder.length() < 4000 && stops.get(stop).getRoute().equals(route); stop++) {
                    stringBuilder.append(first ? "" : "&stops=");
                    stringBuilder.append(stops.get(stop).getRoute());
                    stringBuilder.append("|");
                    stringBuilder.append(stops.get(stop).getName());
                    first = false;
                }

                System.out.println(stringBuilder.toString());
                ResponseEntity<String> response = restTemplate.getForEntity(stringBuilder.toString(), String.class);

                if (response == null) {
                    return;
                } else if (HttpStatus.OK == response.getStatusCode()) {
                    JSONObject object = new JSONObject(response);
                    parseResponse(object.getString("body"), route);
                }
                if(stops.size() > stop) {
                    route = stops.get(stop).getRoute();
                }

            } while(stop != stops.size());
        }
        updateVal = false;
    }

    private void parseResponse(String input, String route) {
        ArrayList<Long> predictions = new ArrayList<>();
        String[] lines = input.split("\n");
        String stop = null;
        for(String line : lines) {
            if(line.contains("<predictions agencyTitle")) {
                stop = getString("stopTag", line);
                predictions = new ArrayList<>();
            } else if(line.contains("prediction epochTime")) {
                predictions.add(Long.valueOf(getString("seconds", line)));
                int count = busStopLocationRepository.countAllByNameAndRoute(stop, route);
                if(count != 0) {
                    BusStopLocation busStopLocation = busStopLocationRepository.findByNameAndRoute(stop, route);
                    busStopLocation.setArrivalTimes(predictions);
                    busStopLocationRepository.save(busStopLocation);
                }
            }
        }
    }

    private static String getString(String value, String line) {
        return line.substring(line.indexOf(value) + value.length() + 2, line.indexOf("\"", line.indexOf(value) + value.length() + 2));
    }


}
