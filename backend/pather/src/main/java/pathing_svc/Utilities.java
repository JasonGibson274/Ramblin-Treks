package pathing_svc;

import pathing_svc.entities.SearchLocation;

public class Utilities {

    public static double distance(SearchLocation searchLocation1, SearchLocation searchLocation2) {
        return Math.abs(Math.sqrt(Math.pow(searchLocation1.getLatitiude() - searchLocation2.getLatitiude(), 2) +
        Math.pow(searchLocation1.getLongitude() - searchLocation2.getLongitude(), 2)));
    }
}
