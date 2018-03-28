package pathing_svc;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pathing_svc.entities.BusStopLocation;
import pathing_svc.entities.BusStopLocationRepository;
import pathing_svc.entities.SearchLocation;
import pathing_svc.entities.SearchLocationRepository;

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


        assertThat(graphSearch.generateSuccessors(parent, mockSearchLocationRepository, null, 0).size(), is(10));
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

        List<SearchLocation> result = graphSearch.aStar(mockSearchLocationRepository, null);
        assertThat(result.size(), is(3));
        assertThat(result.get(0), hasProperty("latitude", is(0.0)));
        assertThat(result.get(0), hasProperty("longitude", is(0.0)));
        assertThat(result.get(1), hasProperty("latitude", is(50.0)));
        assertThat(result.get(1), hasProperty("longitude", is(0.0)));
        assertThat(result.get(2), hasProperty("latitude", is(100.0)));
        assertThat(result.get(2), hasProperty("longitude", is(100.0)));
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
        start.setNeighbors(locations);

        List<BusStopLocation> allBusStopLocations = new ArrayList<>();
        allBusStopLocations.add(a);
        allBusStopLocations.add(b);


        when(mockSearchLocationRepository.findOne(b.getId())).thenReturn(b);
        when(mockSearchLocationRepository.findOne(a.getId())).thenReturn(a);
        when(mockSearchLocationRepository.findOne(start.getId())).thenReturn(start);
        when(mockSearchLocationRepository.findOne(goal.getId())).thenReturn(goal);
        when(mockSearchLocationRepository.count()).thenReturn(4L);
        when(mockBusStopLocationRepository.findAllByRoute("red")).thenReturn(allBusStopLocations);

        List<SearchLocation> result = graphSearch.aStar(mockSearchLocationRepository, mockBusStopLocationRepository);
        /*assertThat(result.size(), is(4));
        assertThat(result.get(0), hasProperty("latitude", is(0.0)));
        assertThat(result.get(0), hasProperty("longitude", is(0.0)));
        assertThat(result.get(1), hasProperty("latitude", is(0.0)));
        assertThat(result.get(1), hasProperty("longitude", is(3.0)));
        assertThat(result.get(2), hasProperty("latitude", is(100.0)));
        assertThat(result.get(2), hasProperty("longitude", is(99.0)));
        assertThat(result.get(3), hasProperty("latitude", is(100.0)));
        assertThat(result.get(3), hasProperty("longitude", is(100.0)));*/
        assertThat(result.size(), is(3));
    }
}
