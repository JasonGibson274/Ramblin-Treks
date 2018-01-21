package pathing_svc;

import org.junit.Test;
import pathing_svc.entities.SearchLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateComparatorTest {

    @Test
    public void getHeuristicCostTest() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitiude(1);
        goal.setLongitude(2);

        StateComparator stateComparator = new StateComparator(goal);

        SearchLocation location = new SearchLocation();
        location.setLongitude(5);
        location.setLatitiude(7);

        double distance = stateComparator.getHeuristicCost(location);
        assertThat(distance, is(6.708203932499369));
    }

    @Test
    public void getPathCostTestEmptyList() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitiude(5);
        goal.setLongitude(5);

        StateComparator stateComparator = new StateComparator(goal);

        double cost = stateComparator.getPathCost(Collections.emptyList());
        assertThat(cost, is(0.0));
    }

    @Test
    public void getPathCostTestNoH() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitiude(5);
        goal.setLongitude(5);

        StateComparator stateComparator = new StateComparator(goal);

        List<SearchLocation> path = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            SearchLocation current = new SearchLocation();
            current.setLatitiude(i + 1);
            current.setLongitude(i + 1);
            path.add(current);
        }
        double cost = stateComparator.getPathCost(path);
        assertThat(cost, is(5.656854249492381));
    }

    @Test
    public void verifyStateComparatorCompare() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitiude(5);
        goal.setLongitude(5);

        StateComparator stateComparator = new StateComparator(goal);
        List<SearchLocation> path1 = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            SearchLocation current = new SearchLocation();
            current.setLatitiude(i + 1);
            current.setLongitude(i + 1);
            path1.add(current);
        }

        List<SearchLocation> path2 = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            SearchLocation current = new SearchLocation();
            current.setLatitiude(i + 1);
            current.setLongitude(i + 1);
            path2.add(current);
        }

        assertThat(stateComparator.compare(path1, path2), is(-1));
        assertThat(stateComparator.compare(path2, path1), is(1));
        assertThat(stateComparator.compare(path1, path1), is(0));
    }
}
