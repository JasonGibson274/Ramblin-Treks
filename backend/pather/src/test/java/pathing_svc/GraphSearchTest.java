package pathing_svc;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pathing_svc.entities.BusStopLocation;
import pathing_svc.entities.BusStopLocationRepository;
import pathing_svc.entities.SearchLocation;
import pathing_svc.entities.SearchLocationRepository;

import javax.transaction.TransactionScoped;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GraphSearchTest {

    @Mock
    private SearchLocationRepository mockSearchLocationRepository = mock(SearchLocationRepository.class);
    @Mock
    private BusStopLocationRepository mockBusStopLocationRepository = mock(BusStopLocationRepository.class);
    private GraphSearch graphSearch;
    private SearchLocation goal;
    private SearchLocation start;

    @Before
    public void setUp() {
        goal = new SearchLocation(UUID.randomUUID(), 100, 100, null);

        start = new SearchLocation(UUID.randomUUID(), 0, 0, null);

        graphSearch = new GraphSearch(start, goal);
    }

    @Test
    public void generateSuccessors() {
        SearchLocation parent = new SearchLocation();
        Map<UUID, SearchLocation> children = new HashMap<>();

        for(int i = 0; i < 10; i++) {
            UUID uuid = UUID.randomUUID();
            SearchLocation temp = new SearchLocation(uuid, 0, 0, null);
            children.put(uuid, temp);
            when(mockSearchLocationRepository.findOne(uuid)).thenReturn(temp);
        }
        parent.setNeighbors(new HashSet<>(children.keySet()));

        assertThat(graphSearch.generateSuccessors(parent, mockSearchLocationRepository, null).size(), is(10));
    }

    @Test
    public void generateSuccessorsBuses() {
        BusStopLocation busStopLocation = new BusStopLocation();
        busStopLocation.setRoute("red");
        busStopLocation.setName("name");
        busStopLocation.setNeighbors(new HashSet<>());
        List<BusStopLocation> stops = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            BusStopLocation busStopLocation1 = new BusStopLocation();
            busStopLocation1.setId(UUID.randomUUID());
            busStopLocation1.setNeighbors(new HashSet<>());
            stops.add(busStopLocation1);
        }
        when(mockBusStopLocationRepository.findAllByRoute("red")).thenReturn(stops);

        assertThat(graphSearch.generateSuccessors(busStopLocation, mockSearchLocationRepository, mockBusStopLocationRepository).size(), is(10));
    }

    @Test
    public void testSearch1() {
        SearchLocation b = new SearchLocation(UUID.randomUUID(), 50, 0, null);
        SearchLocation a = new SearchLocation(UUID.randomUUID(), 0, 100, null);

        Set<UUID> locations = new HashSet<>();
        locations.add(start.getId());
        locations.add(goal.getId());
        b.setNeighbors(locations);
        a.setNeighbors(locations);

        locations = new HashSet<>();
        locations.add(a.getId());
        locations.add(b.getId());
        start.setNeighbors(locations);

        when(mockSearchLocationRepository.findOne(b.getId())).thenReturn(b);
        when(mockSearchLocationRepository.findOne(a.getId())).thenReturn(a);
        when(mockSearchLocationRepository.findOne(start.getId())).thenReturn(start);
        when(mockSearchLocationRepository.findOne(goal.getId())).thenReturn(goal);
        when(mockSearchLocationRepository.count()).thenReturn(4L);
        when(mockBusStopLocationRepository.count()).thenReturn(0L);

        Path result = graphSearch.aStar(mockSearchLocationRepository, mockBusStopLocationRepository);
        assertThat(result.getLocations().size(), is(3));
        assertThat(result.getLocations().get(0), hasProperty("latitude", is(0.0)));
        assertThat(result.getLocations().get(0), hasProperty("longitude", is(0.0)));
        assertThat(result.getLocations().get(1), hasProperty("latitude", is(50.0)));
        assertThat(result.getLocations().get(1), hasProperty("longitude", is(0.0)));
        assertThat(result.getLocations().get(2), hasProperty("latitude", is(100.0)));
        assertThat(result.getLocations().get(2), hasProperty("longitude", is(100.0)));
    }

    @Test
    public void testSearch1WithBuses() {

        BusStopLocation b = new BusStopLocation(UUID.randomUUID(), 0.0, 0.1, null, "color", "name", "red");
        BusStopLocation a = new BusStopLocation(UUID.randomUUID(), 100.0, 99.0,  null, "color", "name", "red");

        Set<UUID> locations = new HashSet<>();
        locations.add(start.getId());
        locations.add(goal.getId());
        b.setNeighbors(locations);
        a.setNeighbors(locations);

        locations = new HashSet<>();
        locations.add(a.getId());
        locations.add(b.getId());
        start.setNeighbors(locations);

        List<BusStopLocation> allBusStopLocations = new ArrayList<>();
        allBusStopLocations.add(a);
        allBusStopLocations.add(b);

        List<Long> arrivalTimes = new ArrayList<>();
        arrivalTimes.add(8000L);
        arrivalTimes.add(9000L);
        a.setArrivalTimes(arrivalTimes);
        b.setArrivalTimes(arrivalTimes);


        when(mockSearchLocationRepository.findOne(b.getId())).thenReturn(b);
        when(mockSearchLocationRepository.findOne(a.getId())).thenReturn(a);
        when(mockSearchLocationRepository.findOne(start.getId())).thenReturn(start);
        when(mockSearchLocationRepository.findOne(goal.getId())).thenReturn(goal);
        when(mockSearchLocationRepository.count()).thenReturn(4L);
        when(mockBusStopLocationRepository.count()).thenReturn(2L);
        when(mockBusStopLocationRepository.findAllByRoute("red")).thenReturn(allBusStopLocations);

        Path result = graphSearch.aStar(mockSearchLocationRepository, mockBusStopLocationRepository);
        assertThat(result.getLocations().size(), is(4));
        assertThat(result.getLocations().get(0), hasProperty("latitude", is(0.0)));
        assertThat(result.getLocations().get(0), hasProperty("longitude", is(0.0)));
        assertThat(result.getLocations().get(1), hasProperty("latitude", is(0.0)));
        assertThat(result.getLocations().get(1), hasProperty("longitude", is(0.1)));
        assertThat(result.getLocations().get(2), hasProperty("latitude", is(100.0)));
        assertThat(result.getLocations().get(2), hasProperty("longitude", is(99.0)));
        assertThat(result.getLocations().get(3), hasProperty("latitude", is(100.0)));
        assertThat(result.getLocations().get(3), hasProperty("longitude", is(100.0)));
        assertThat(result.getCost(), is(22808.409566221126));
    }
}
