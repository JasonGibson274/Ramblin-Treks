package pathing_svc;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(PathingController.class)
public class PathingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PathingController pathingController;

    @Test
    public void testExample() {
        JSONObject jsonObject = new JSONObject();


        /*Employee alex = new Employee("alex");
        List<Employee> allEmployees = Arrays.asList(alex);
        given(service.getAllEmployees()).willReturn(allEmployees);
        mvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(alex.getName())));*/
    }
}
