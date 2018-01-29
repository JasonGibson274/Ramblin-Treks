package pathing_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SearchLocationRepository extends CrudRepository<SearchLocation, UUID> {
}
