package pathing_svc;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pathing_svc.entities.PathingRequest;
import pathing_svc.entities.PathingRequestRepository;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = PathingService.class)
public class PathingServiceTest {

    @Autowired
    private PathingService pathingService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PathingRequestRepository pathingRequestRepository;

    @Test
    public void findOrCreateTestNullId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startLatitude", 34.11111);
        jsonObject.put("endLatitude", 84.11111);
        jsonObject.put("startLongitude", 34.55555);
        jsonObject.put("endLongitude", 84.55555);

        PathingRequest pathingRequest = pathingService.findOrCreate(null, jsonObject);
        assertThat(pathingRequest, hasProperty("id", notNullValue()));

        assertThat(pathingRequestRepository.findOne(pathingRequest.getId()), is(pathingRequest));
    }

    @Test
    public void findOrCreateTestAlreadyExists() {
        UUID id = UUID.randomUUID();
        PathingRequest pathingRequest = new PathingRequest(id, 34.11111, 84.55555, 84.11111, 84.55555);
        testEntityManager.getEntityManager().persist(pathingRequest);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id.toString());
        jsonObject.put("startLatitude", 34.11111);
        jsonObject.put("endLatitude", 34.55555);
        jsonObject.put("startLongitude", 84.11111);
        jsonObject.put("endLongitude", 84.55555);

        PathingRequest result = pathingService.findOrCreate(id, jsonObject);
        assertThat(pathingRequestRepository.count(), is(1L));
        assertThat(pathingRequest, hasProperty("id", is(id)));
        assertThat(pathingRequestRepository.findOne(pathingRequest.getId()), is(pathingRequest));
    }

    @Test
    public void findOrCreateTestAlreadyExistsDifferentDestination() {
        UUID id = UUID.randomUUID();
        PathingRequest pathingRequest = new PathingRequest(id, 34.1111, 84.2222, 84.1111, 84.2222);
        testEntityManager.getEntityManager().persist(pathingRequest);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id.toString());
        jsonObject.put("startLatitude", 34.11111);
        jsonObject.put("endLatitude", 34.55555);
        jsonObject.put("startLongitude", 84.1111);
        jsonObject.put("endLongitude", 84.55555);

        pathingService.findOrCreate(id, jsonObject);
        assertThat(pathingRequestRepository.count(), is(1L));
        assertThat(pathingRequest, hasProperty("id", is(id)));
        assertThat(pathingRequestRepository.findOne(pathingRequest.getId()), hasProperty("endLongitude", is(84.55555)));
        assertThat(pathingRequestRepository.findOne(pathingRequest.getId()), hasProperty("endLatitude", is(34.55555)));
        // TODO ensure cache is clear
    }
}
