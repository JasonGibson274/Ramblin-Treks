package data_svc;

import data_svc.entities.PathLocation;
import data_svc.entities.PathLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class DataService {

    private final PathLocationRepository pathLocationRepository;

    @Autowired
    public DataService(PathLocationRepository pathLocationRepository) {
        this.pathLocationRepository = pathLocationRepository;
    }

    public void saveLocation(double latitude, double longitude) {
        final PathLocation path = new PathLocation(latitude, longitude);
        pathLocationRepository.save(path);
    }

    public List<PathLocation> findAllPathLocations() {
        return pathLocationRepository.findAll();
    }

    public void createCSV(HttpServletResponse response) throws IOException {
        String csvFileName = "locations.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        List<PathLocation> locations = findAllPathLocations();

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "latitude", "longitude"};

        csvWriter.writeHeader(header);

        for (PathLocation loc : locations) {
            //String[] temp = {"getId"};
            csvWriter.write(loc, header);
        }

        csvWriter.close();
    }

    public void saveCsv(String csv) {
        csv = csv.replaceFirst(".*\n", "");
        for(String line : csv.split("\n")) {
            String[] temp = line.split(",");
            saveLocation(new Double(temp[1]), new Double(temp[2]));
        }
    }
}
