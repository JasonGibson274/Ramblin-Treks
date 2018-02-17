package data_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BusPositionRepository extends CrudRepository<BusPosition, UUID> {
    List<BusPosition> findAll();
    List<BusPosition> findAllByBusIdAndFullFlag(String busId, Boolean fullFlag);
    List<BusPosition> findAllByFullFlag(Boolean fullFlag);
}
