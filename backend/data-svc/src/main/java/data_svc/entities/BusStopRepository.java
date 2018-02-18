package data_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BusStopRepository extends CrudRepository<BusStop, String> {
    List<BusStop> findAllByBusRoute(BusRoute busRoute);
    List<BusStop> findAll();
}
