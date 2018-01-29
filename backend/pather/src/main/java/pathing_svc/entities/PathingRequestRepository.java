package pathing_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PathingRequestRepository extends CrudRepository<PathingRequest, UUID> {

}
