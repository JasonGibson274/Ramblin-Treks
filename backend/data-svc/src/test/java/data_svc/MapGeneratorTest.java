package data_svc;

import data_svc.entities.BusStop;
import data_svc.entities.PathLocation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class MapGeneratorTest {

    private MapGenerator mapGenerator;

    private double MIN_LATITUDE = 0.0;
    private double MAX_LATITUDE = 100.0;
    private double MIN_LONGITUDE = 0.0;
    private double MAX_LONGITUDE = 100.0;
    private double SEPARATION_DIST = 90000;
    private double VOXEL_RESOLUTION = 2;
    private final double MIN_PATH_LENGTH = 1;

    @Before
    public void setUp() {
        this.mapGenerator = new MapGenerator(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE, MAX_LONGITUDE, SEPARATION_DIST, VOXEL_RESOLUTION, MIN_PATH_LENGTH);
    }

    @Test
    public void voxelGridIgnoresExternalPoints() {
        List<PathLocation> testCases = new ArrayList<>();
        testCases.add(new PathLocation(100.5, 0.0));
        testCases.add(new PathLocation(0.0, 100.5));
        testCases.add(new PathLocation(-0.5, 0.0));
        testCases.add(new PathLocation(0.0, -0.5));
        testCases.add(new PathLocation(0.0, 0.0));

        List<PathLocation> result = mapGenerator.voxelGrid(testCases);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), hasProperty("latitude", is(0.0)));
        assertThat(result.get(0), hasProperty("longitude", is(0.0)));
    }

    @Test
    public void voxelGridIgnoresDuplicatePoints() {
        List<PathLocation> testCases = new ArrayList<>();
        for(double i = 0; i < VOXEL_RESOLUTION; i += VOXEL_RESOLUTION / 10) {
            testCases.add(new PathLocation(MIN_LATITUDE + i, MIN_LONGITUDE + VOXEL_RESOLUTION / 2));
            testCases.add(new PathLocation(MIN_LATITUDE + VOXEL_RESOLUTION / 2, MIN_LONGITUDE + i));
        }

        List<PathLocation> result = mapGenerator.voxelGrid(testCases);
        assertThat(result.size(), is(3));
    }

    @Test
    public void voxelGridAddsToOtherGrids() {
        List<PathLocation> testCases = new ArrayList<>();
        for(double i = 0; i < VOXEL_RESOLUTION * 2; i += VOXEL_RESOLUTION / 10) {
            testCases.add(new PathLocation(MIN_LATITUDE + i, MIN_LONGITUDE + VOXEL_RESOLUTION / 2));
            testCases.add(new PathLocation(MIN_LATITUDE + VOXEL_RESOLUTION / 2, MIN_LONGITUDE + i));
        }

        List<PathLocation> result = mapGenerator.voxelGrid(testCases);
        assertThat(result.size(), is(5));
    }

    @Test
    public void createsNeighborsWhenClose() {
        List<PathLocation> testCases = new ArrayList<>();
        testCases.add(new PathLocation(UUID.randomUUID(), 0.1, 0.0));
        testCases.add(new PathLocation(UUID.randomUUID(), 0.5, 0.0));
        testCases.add(new PathLocation(UUID.randomUUID(), 0.0, 0.6));
        testCases.add(new PathLocation(UUID.randomUUID(), 0.3, 0.3));

        Map<PathLocation, List<UUID>> result = mapGenerator.findNeighbors(testCases, new ArrayList<BusStop>());
        assertThat(result.keySet().size(), is(4));

        for(PathLocation current : result.keySet()) {
            assertThat(result.get(current).size(), is(3));
            for(UUID currentId : result.get(current)) {
                assertThat(current.getId(), is(not(currentId)));
            }
        }
    }

    @Test
    public void doeNotCreateNeighorIfFar() {
        List<PathLocation> testCases = new ArrayList<>();
        testCases.add(new PathLocation(UUID.randomUUID(), 0.0, 0.0));
        testCases.add(new PathLocation(UUID.randomUUID(), 1.5, 0.0));
        testCases.add(new PathLocation(UUID.randomUUID(), 0.0, 1.6));
        testCases.add(new PathLocation(UUID.randomUUID(), 2.7, 2.7));

        Map<PathLocation, List<UUID>> result = mapGenerator.findNeighbors(testCases, new ArrayList<BusStop>());
        assertThat(result.keySet().size(), is(0));
    }



}
