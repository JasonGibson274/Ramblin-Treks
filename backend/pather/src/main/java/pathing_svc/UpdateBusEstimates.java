package pathing_svc;

import io.swagger.models.auth.In;
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

    public UpdateBusEstimates(RestTemplate restTemplate, BusStopLocationRepository busStopLocationRepository) {
        this.restTemplate = restTemplate;
        this.busStopLocationRepository = busStopLocationRepository;
    }

    @Override
    public void run() {
        String url = "https://gtbuses.herokuapp.com/agencies/georgia-tech/multiPredictions?stops=";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        boolean first = true;
        for (BusStopLocation busstop : busStopLocationRepository.findAll()) {
            stringBuilder.append(first ? "?" : "&");
            stringBuilder.append(busstop.getRoute());
            stringBuilder.append("|");
            stringBuilder.append(busstop.getName());
            first = false;
        }
        ResponseEntity<String> response = restTemplate.getForEntity(stringBuilder.toString(), String.class);

        if(response == null) {
            return;
        } else if (HttpStatus.OK == response.getStatusCode()) {
            JSONObject object = new JSONObject(response);
            parseResponse(object.getString("body"));
        }
    }

    private void parseResponse(String input) {
        ArrayList<Long> predictions = new ArrayList<>();
        String[] lines = input.split("\n");
        String stop = null;
        for(String line : lines) {
            if(line.contains("<predictions agencyTitle")) {
                stop = getString("stopTag", line);
                predictions.clear();
            } else if(line.contains("<prediction epochTime")) {
                predictions.add(Long.valueOf(getString("seconds", line)));
                busStopLocationRepository.findByName(stop).setArrivalTimes(predictions);
            }
        }

    }

    private static String getString(String value, String line) {
        return line.substring(line.indexOf(value) + value.length() + 2, line.indexOf("\"", line.indexOf(value) + value.length() + 2));
    }


}
