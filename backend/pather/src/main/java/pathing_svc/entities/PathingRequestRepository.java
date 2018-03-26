package pathing_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PathingRequestRepository extends CrudRepository<PathingRequest, UUID> {
    List<PathingRequest> findAllByTimeStampBefore(Date time);
    PathingRequest findFirstByTimeStamp(Date time);
}
