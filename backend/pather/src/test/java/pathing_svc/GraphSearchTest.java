package pathing_svc;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pathing_svc.entities.PathingRequestRepository;
import pathing_svc.entities.SearchLocation;
import pathing_svc.entities.SearchLocationRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GraphSearchTest {

    @Mock
    private SearchLocationRepository mockSearchLocationRepository = mock(SearchLocationRepository.class);
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


        assertThat(graphSearch.generateSuccessors(parent, mockSearchLocationRepository).size(), is(10));
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

        List<SearchLocation> result = graphSearch.aStar(mockSearchLocationRepository);
        assertThat(result.size(), is(3));
        assertThat(result.get(0), hasProperty("latitude", is(0.0)));
        assertThat(result.get(0), hasProperty("longitude", is(0.0)));
        assertThat(result.get(1), hasProperty("latitude", is(50.0)));
        assertThat(result.get(1), hasProperty("longitude", is(0.0)));
        assertThat(result.get(2), hasProperty("latitude", is(100.0)));
        assertThat(result.get(2), hasProperty("longitude", is(100.0)));
    }
}
