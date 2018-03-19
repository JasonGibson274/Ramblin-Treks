package data_svc;

import data_svc.entities.*;
import jdk.nashorn.api.scripting.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {DataService.class, BusPositionRepository.class})
public class BusDataLoggingTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BusPositionRepository busPositionRepository;

    @Autowired
    private BusRouteRepository busRouteRepository;

    @Autowired
    private BusStopRepository busStopRepository;

    @MockBean
    private RestTemplate restTemplate;


    private BusRoute busRoute;

    @Before
    public void setUp() {
        testEntityManager.clear();
        busRoute = new BusRoute("trolley", "route Name", "#ffffff");
        testEntityManager.merge(busRoute);
        //Long id, String stopName, double latitude, double longitude, String direction
        BusStop busStop = new BusStop(1L, "stop 1", 1.0, 1.0, "dir");
        busStop.setBusRoute("trolley");
        testEntityManager.merge(busStop);
        busStop = new BusStop(2L, "stop 2", 2.0, 2.0, "dir");
        busStop.setBusRoute("trolley");
        testEntityManager.merge(busStop);
    }

    @Test
    public void parseResponseTest() {
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "<body copyright=\"All data copyright Georgia Tech Campus 2018.\">\n" +
                "<vehicle id=\"408\" routeTag=\"trolley\" dirTag=\"marta\" lat=\"33.777615\" lon=\"-84.395848\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"11\"/>\n" +
                "<lastTime time=\"1518826554667\"/>\n" +
                "<keyForNextTime value=\"1859447479423\" />\n" +
                "<config iOSVersion=\"3.0.7\" party=\"false\" message=\"\" />\n" +
                "</body>";
        GtBusesApiCalls.parseResponse(body, busRouteRepository, busPositionRepository, busStopRepository);

        assertThat(busPositionRepository.count(), is(1L));
        BusPosition busPosition = busPositionRepository.findAll().get(0);
        assertThat(busPosition.getBusId(), is("408"));
        assertThat(busPosition.getBusRoute(), is("trolley"));
        assertThat(busPosition.getHeading(), is(166.0));
        assertThat(busPosition.getSpeed(), is(11.0));
        assertThat(busPosition.getLatitude(), is(33.777615));
        assertThat(busPosition.getLongitude(), is(-84.395848));
    }

    /*@Test
    public void parseResponseMultipleTest() {
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "<body copyright=\"All data copyright Georgia Tech Campus 2018.\">\n" +
                "<vehicle id=\"408\" routeTag=\"trolley\" dirTag=\"marta\" lat=\"33.777615\" lon=\"-84.395848\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"11\"/>\n" +
                "<vehicle id=\"683\" routeTag=\"red\" dirTag=\"marta\" lat=\"33.777615\" lon=\"-84.395848\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"11\"/>\n" +
                "<lastTime time=\"1518826554667\"/>\n" +
                "<keyForNextTime value=\"1859447479423\" />\n" +
                "<config iOSVersion=\"3.0.7\" party=\"false\" message=\"\" />\n" +
                "</body>";
        GtBusesApiCalls.parseResponse(body, busRouteRepository, busPositionRepository, busStopRepository);

        assertThat(busPositionRepository.count(), is(2L));
    }

    @Test
    public void updateArrivalTimeNothing() {
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "<body copyright=\"All data copyright Georgia Tech Campus 2018.\">\n" +
                "<vehicle id=\"408\" routeTag=\"trolley\" dirTag=\"marta\" lat=\"33.78082\" lon=\"-84.38641\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"11\"/>\n" +
                "<lastTime time=\"1518826554667\"/>\n" +
                "<keyForNextTime value=\"1859447479423\" />\n" +
                "<config iOSVersion=\"3.0.7\" party=\"false\" message=\"\" />\n" +
                "</body>";
        ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplate.getForEntity("https://gtbuses.herokuapp.com/agencies/georgia-tech/vehicles", String.class)).thenReturn(response);
        dataService.grabAllBusPositions();

        assertThat(busPositionRepository.count(), is(1L));
        BusPosition result = busPositionRepository.findAll().get(0);
        assertThat(result.getFullFlag(), is(Boolean.FALSE));
    }

    @Test
    public void updateArrivalTimeArrived() {
        BusPosition busPosition = new BusPosition("408", 1.0, 1.0, 100.0, 100.0, "trolley", null, Boolean.FALSE);
        testEntityManager.persist(busPosition);

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "<body copyright=\"All data copyright Georgia Tech Campus 2018.\">\n" +
                "<vehicle id=\"408\" routeTag=\"trolley\" dirTag=\"marta\" lat=\"1.0\" lon=\"1.0\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"0\"/>\n" +
                "<lastTime time=\"1518826554667\"/>\n" +
                "<keyForNextTime value=\"1859447479423\" />\n" +
                "<config iOSVersion=\"3.0.7\" party=\"false\" message=\"\" />\n" +
                "</body>";
        ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplate.getForEntity("https://gtbuses.herokuapp.com/agencies/georgia-tech/vehicles", String.class)).thenReturn(response);
        dataService.grabAllBusPositions();

        assertThat(busPositionRepository.count(), is(2L));
        BusPosition result = busPositionRepository.findOne(busPosition.getId());
        assertThat(result.getFullFlag(), is(Boolean.FALSE));
        assertThat(result.getArrivalTimesMap().size(), is(1));
        assertThat(result.getArrivalTimesMap().get(1L), greaterThan(0L));
    }

    @Test
    public void updateArrivalTimeArrivedIgnoreFull() {
        BusPosition busPosition = new BusPosition("1", 0.0, 0.0, 100.0, 100.0, "trolley", null, Boolean.TRUE);
        testEntityManager.persist(busPosition);

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "<body copyright=\"All data copyright Georgia Tech Campus 2018.\">\n" +
                "<vehicle id=\"408\" routeTag=\"trolley\" dirTag=\"marta\" lat=\"1.0\" lon=\"1.0\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"0\"/>\n" +
                "<lastTime time=\"1518826554667\"/>\n" +
                "<keyForNextTime value=\"1859447479423\" />\n" +
                "<config iOSVersion=\"3.0.7\" party=\"false\" message=\"\" />\n" +
                "</body>";
        ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplate.getForEntity("https://gtbuses.herokuapp.com/agencies/georgia-tech/vehicles", String.class)).thenReturn(response);
        dataService.grabAllBusPositions();

        assertThat(busPositionRepository.count(), is(2L));
        BusPosition result = busPositionRepository.findOne(busPosition.getId());
        assertThat(result.getFullFlag(), is(Boolean.TRUE));
        assertThat(result.getArrivalTimesMap().size(), is(0));
    }

    @Test
    public void updateArrivalTimeArrivedBothStops() {
        BusPosition busPosition = new BusPosition("408", 0.0, 0.0, 100.0, 100.0, "trolley", null, Boolean.FALSE);
        testEntityManager.persist(busPosition);

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "<body copyright=\"All data copyright Georgia Tech Campus 2018.\">\n" +
                "<vehicle id=\"408\" routeTag=\"trolley\" dirTag=\"marta\" lat=\"1.0\" lon=\"1.0\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"0\"/>\n" +
                "<lastTime time=\"1518826554667\"/>\n" +
                "<keyForNextTime value=\"1859447479423\" />\n" +
                "<config iOSVersion=\"3.0.7\" party=\"false\" message=\"\" />\n" +
                "</body>";
        ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.getForEntity("https://gtbuses.herokuapp.com/agencies/georgia-tech/vehicles", String.class)).thenReturn(response);
        dataService.grabAllBusPositions();

        body = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "<body copyright=\"All data copyright Georgia Tech Campus 2018.\">\n" +
                "<vehicle id=\"408\" routeTag=\"trolley\" dirTag=\"marta\" lat=\"2.0\" lon=\"2.0\" secsSinceReport=\"1\" predictable=\"true\" heading=\"166\" speedKmHr=\"0\"/>\n" +
                "<lastTime time=\"1518826554667\"/>\n" +
                "<keyForNextTime value=\"1859447479423\" />\n" +
                "<config iOSVersion=\"3.0.7\" party=\"false\" message=\"\" />\n" +
                "</body>";
        response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.getForEntity("https://gtbuses.herokuapp.com/agencies/georgia-tech/vehicles", String.class)).thenReturn(response);
        dataService.grabAllBusPositions();

        assertThat(busPositionRepository.count(), is(3L));
        BusPosition result = busPositionRepository.findOne(busPosition.getId());
        assertThat(result.getFullFlag(), is(Boolean.TRUE));
        assertThat(result.getArrivalTimesMap().size(), is(2));
        assertThat(result.getArrivalTimesMap().get(1L), greaterThan(0L));
        assertThat(result.getArrivalTimesMap().get(2L), greaterThan(0L));
    }*/

    @Test
    public void test() {
        assertThat(true, is(true));
    }
}
