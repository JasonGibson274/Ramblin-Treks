package pathing_svc.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BusStopLocationRepository extends CrudRepository<BusStopLocation, UUID> {
    List<BusStopLocation> findAllByRoute(String route);
    BusStopLocation findByNameAndRoute(String name, String route);
    List<BusStopLocation> findAll();
    List<BusStopLocation> findAllByOrderByRoute();
    int countAllByNameAndRoute(String name, String route);
}
