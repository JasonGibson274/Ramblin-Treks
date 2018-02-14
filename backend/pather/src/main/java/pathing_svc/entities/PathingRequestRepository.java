package pathing_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PathingRequestRepository extends CrudRepository<PathingRequest, UUID> {
    public List<PathingRequest> findAllByTimeStampBefore(Date time);
}
