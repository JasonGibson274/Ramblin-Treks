package data_svc;

import data_svc.entities.PathLocation;
import data_svc.entities.PathLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
