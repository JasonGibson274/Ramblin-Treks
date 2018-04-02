package data_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PathLocationRepository extends CrudRepository<PathLocation, UUID> {
    List<PathLocation> findAll();
}
