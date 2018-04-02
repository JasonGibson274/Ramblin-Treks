package pathing_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SearchLocationRepository extends CrudRepository<SearchLocation, UUID> {
    List<SearchLocation> findAll();
}
