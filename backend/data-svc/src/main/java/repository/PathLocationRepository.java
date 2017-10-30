package repository;

import entity.PathLocation;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PathLocationRepository extends CrudRepository<PathLocation, UUID> {
}
