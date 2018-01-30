package trek_utils;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TrekUtils {
    public static double getDistanceInMetersHaversine(double startLat, double startLon, double endLat, double endLon) {
        double R = 6378137;

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLon - startLon));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    public static double getBearing(double latitude1, double longitude1, double latitude2, double longitude2) {
        double y = Math.sin(longitude2 - longitude1) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longitude2 - longitude1);
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    public static void createCsv(HttpServletResponse response, List<Object> list) throws IOException {
        String csvFileName = "locations.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "latitude", "longitude"};

        csvWriter.writeHeader(header);

        for(Object cur : list) {
            csvWriter.write(cur, header);
        }

        csvWriter.close();
    }
}
