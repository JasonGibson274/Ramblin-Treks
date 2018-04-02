package pathing_svc;

import org.junit.Test;
import pathing_svc.entities.BusStopLocation;
import pathing_svc.entities.SearchLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateComparatorTest {

    @Test
    public void getHeuristicCostTest() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(1);
        goal.setLongitude(2);

        StateComparator stateComparator = new StateComparator(goal);

        SearchLocation location = new SearchLocation();
        location.setLongitude(5);
        location.setLatitude(7);

        double distance = stateComparator.getHeuristicCost(location);
        assertThat(distance, is(533086.6765765811));
    }

    @Test
    public void getPathCostTestEmptyList() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(5);
        goal.setLongitude(5);

        Path path = new Path();

        StateComparator stateComparator = new StateComparator(goal);

        double cost = stateComparator.getDistanceCost(path, null);
        assertThat(cost, is(0.0));
    }

    @Test
    public void getPathCostTestNoHNoInitial() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(5);
        goal.setLongitude(5);

        Path path = new Path();

        path.setLocations(Collections.singletonList(new SearchLocation(0.0, 0.0)));

        StateComparator stateComparator = new StateComparator(goal);


        double cost = stateComparator.getDistanceCost(path, new SearchLocation(1.0, 1.0));
        assertThat(cost, is(112446.8122202943));
    }

    @Test
    public void getPathCostTestNoHWithInitial() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(5);
        goal.setLongitude(5);

        Path path = new Path();
        path.setCost(1000);

        path.setLocations(Collections.singletonList(new SearchLocation(0.0, 0.0)));
        StateComparator stateComparator = new StateComparator(goal);

        double cost = stateComparator.getDistanceCost(path, new SearchLocation(1.0, 1.0));
        assertThat(cost, is(112446.8122202943 + 1000));
    }

    @Test
    public void getPathCostTestWithHWithInitial() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(5);
        goal.setLongitude(5);

        SearchLocation added = new SearchLocation(1.0, 1.0);

        Path path = new Path();
        path.setCost(1000);

        path.setLocations(Collections.singletonList(new SearchLocation(0.0, 0.0)));
        StateComparator stateComparator = new StateComparator(goal);

        double cost = stateComparator.getDistanceCost(path, added);
        assertThat(cost, is(112446.8122202943 + 1000));
        double heuristic = stateComparator.getHeuristicCost(added);
        assertThat(heuristic, is(449444.46772595996));
    }

    @Test
    public void verifyStateComparatorCompare() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(5);
        goal.setLongitude(5);
        StateComparator stateComparator = new StateComparator(goal);

        Path path1 = new Path();
        path1.setCost(1000);

        Path path2 = new Path();
        path2.setCost(10000);

        assertThat(stateComparator.compare(path1, path2), is(-1));
        assertThat(stateComparator.compare(path2, path1), is(1));
        assertThat(stateComparator.compare(path1, path1), is(0));
    }

    @Test
    public void getPathCostWithBusesMissesBus() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(5);
        goal.setLongitude(5);
        StateComparator stateComparator = new StateComparator(goal);

        ArrayList<SearchLocation> locations = new ArrayList<>();
        BusStopLocation stopStart = new BusStopLocation(UUID.randomUUID(), 1.0,1.0, null);
        stopStart.setArrivalTimes(Collections.singletonList(100L));
        locations.add(stopStart);
        Path path = new Path(locations, 500);

        BusStopLocation stopEnd = new BusStopLocation(UUID.randomUUID(), 5.0,5.0, null);
        stopEnd.setArrivalTimes(Collections.singletonList(200L));

        assertThat(stateComparator.getDistanceCost(path, stopEnd), is(449944.46772595996));
    }

    @Test
    public void getPathCostWithBuses() {
        SearchLocation goal = new SearchLocation();
        goal.setLatitude(5);
        goal.setLongitude(5);
        StateComparator stateComparator = new StateComparator(goal);

        ArrayList<SearchLocation> locations = new ArrayList<>();
        BusStopLocation stopStart = new BusStopLocation(UUID.randomUUID(), 1.0,1.0, null);
        stopStart.setArrivalTimes(Collections.singletonList(600L));
        locations.add(stopStart);

        Path path = new Path(locations, 500);

        BusStopLocation stop = new BusStopLocation(UUID.randomUUID(), 5.0,5.0, null);
        stop.setArrivalTimes(Collections.singletonList(1000L));

        assertThat(stateComparator.getDistanceCost(path, stop), is(1000.0));
    }
}
