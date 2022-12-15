package pl.pwr.peaklogistic.acceptance;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pwr.peaklogistic.PeakLogisticApplication;
import pl.pwr.peaklogistic.dto.request.jobOffer.PostJobOffer;
import pl.pwr.peaklogistic.dto.request.jobOffer.PutJobOffer;
import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;
import pl.pwr.peaklogistic.dto.request.user.*;
import pl.pwr.peaklogistic.dto.response.CustomerResponse;
import pl.pwr.peaklogistic.dto.response.TransportOrderResponse;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.services.JobOfferService;
import pl.pwr.peaklogistic.services.TransportOfferService;
import pl.pwr.peaklogistic.services.TransportOrderService;
import pl.pwr.peaklogistic.services.UserService;


import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PeakLogisticApplication.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class SignUpControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Before
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        createUsers();
    }

    private void createUsers(){
        PostUser postUser = new PostUser("admin@admin.org", "pass1");
        userService.createAdmin(postUser);
    }

    private PostUser generateUser(){
        return new PostUser("admin1@admin.org", "pass1");
    }

    private PostCustomer generateCustomer(){
        return new PostCustomer("customer@customer.org", "pass1", "nickname");
    }

    private PostCarrier generateCarrier(){
        return new PostCarrier("carrier@carrier.org", "pass1", "transportex", "+45 345678789", "1234567890");

    }

    private PostJobOffer generateJobOffer(){
        return new PostJobOffer("tytul", "title", "tresc", "content", "contact@email.com");
    }

    private byte[] convertToJSON(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    private String convertToJSONString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

    /** POST - create customer **/

    @Test
    @Rollback
    @WithMockUser(username = "-", password = "-")
    public void givenAnyUser_whenCreateCustomer_thenStatus201()
            throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/customers").
                                contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJSON(generateCustomer())));

        resultActions.andExpect(status().isCreated());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "{\"userID\":3,\"email\":\"customer@customer.org\",\"nickname\":\"nickname\"}";

        assertEquals(expected, result);
    }


    /** POST - create carrier **/


    @Test
    @Rollback
    @WithMockUser(username = "-", password = "-")
    public void givenAnyUser_whenCreateCarrier_thenStatus201()
            throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/carriers").
                                contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJSON(generateCarrier())));

        resultActions.andExpect(status().isCreated());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "{\"userID\":4,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"}";

        assertEquals(expected, result);
    }


    /** POST - create admin **/

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenAdmin_whenCreateAdmin_thenStatus201()
            throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/admins").
                                contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJSON(generateUser())));

        resultActions.andExpect(status().isCreated());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        String expected = "{\"userID\":2,\"email\":\"admin1@admin.org\",\"userType\":\"Admin\",\"nickname\":null,\"companyName\":null,\"phone\":null,\"taxIdentificationNumber\":null}";

        assertEquals(expected, result);
    }

    @Test
    @Rollback
    @WithMockUser(username = "-", password = "-")
    public void givenNotAdmin_whenCreateAdmin_thenStatus201() throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/admins").
                                contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJSON(generateUser())));

        resultActions.andExpect(status().isForbidden());
    }
}
