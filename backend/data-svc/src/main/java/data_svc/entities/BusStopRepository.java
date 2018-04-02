package data_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusStopRepository extends CrudRepository<BusStop, String> {
    List<BusStop> findAllByBusRoute(String busRoute);
    List<BusStop> findAll();
}
