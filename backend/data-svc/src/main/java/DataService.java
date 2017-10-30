import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PathLocationRepository;

@Service
public class DataService {

    private final PathLocationRepository pathLocationRepository;

    @Autowired
    public DataService(PathLocationRepository pathLocationRepository) {
        this.pathLocationRepository = pathLocationRepository;
    }

}
