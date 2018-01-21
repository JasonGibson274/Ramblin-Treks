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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GraphSearchTest {

    @Mock
    SearchLocationRepository mockSearchLocationRepository = mock(SearchLocationRepository.class);
    private GraphSearch graphSearch;

    @Before
    public void setUp() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitiude(100);
        goal.setLongitude(100);

        SearchLocation start = new SearchLocation();
        start.setLatitiude(0);
        start.setLongitude(0);

        graphSearch = new GraphSearch(start, goal);
    }

    @Test
    public void generateSuccessors() {
        SearchLocation parent = new SearchLocation();
        Map<UUID, SearchLocation> children = new HashMap<>();

        for(int i = 0; i < 10; i++) {
            UUID uuid = UUID.randomUUID();
            SearchLocation temp = new SearchLocation();
            children.put(uuid, temp);
            when(mockSearchLocationRepository.findOne(uuid)).thenReturn(temp);
        }
        parent.setNeighbors(new HashSet<>(children.keySet()));


        assertThat(graphSearch.generateSuccessors(parent, mockSearchLocationRepository).size(), is(10));
    }

    @Test
    public void testSearch1() {
        
    }
}
