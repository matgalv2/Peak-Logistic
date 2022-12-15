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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pwr.peaklogistic.PeakLogisticApplication;
import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;
import pl.pwr.peaklogistic.dto.request.user.*;
import pl.pwr.peaklogistic.services.TransportOfferService;
import pl.pwr.peaklogistic.services.TransportOrderService;
import pl.pwr.peaklogistic.services.UserService;


import java.io.IOException;
import java.sql.Date;

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
public class TransportOfferControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserService userService;
    @Autowired
    private TransportOrderService orderService;
    @Autowired
    private TransportOfferService offerService;



    @Before
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        createUsers();
        orderService.createTransportOrder(generateOrder(), 2);
    }

    private void createUsers(){
        PostUser postUser = new PostUser("admin@admin.org", "pass1");
        userService.createAdmin(postUser);

        PostCustomer postCustomer = new PostCustomer("customer@customer.org", "pass1", "nickname");
        userService.createCustomer(postCustomer);

        PostCarrier postCarrier = new PostCarrier("carrier@carrier.org", "pass1", "transportex", "+45 345678789", "1234567890");
        userService.createCarrier(postCarrier);
    }

    private PostTransportOrder generateOrder(){

        PostTransportOrder order = new PostTransportOrder();
        order.setCompleted(false);
        order.setFromLocation("a");
        order.setToLocation("b");
        order.setStartDateFrom(Date.valueOf("2022-3-28"));
        order.setStartDateTo(Date.valueOf("2022-3-28"));
        order.setEndDateFrom(Date.valueOf("2022-3-28"));
        order.setEndDateTo(Date.valueOf("2022-3-28"));
        order.setProductWeightKG(21.3f);
        order.setProductHeightCM(100);
        order.setProductWidthCM(100);
        order.setProductDepthCM(100);

        return order;
    }

    private PostTransportOffer generateTransportOffer(){
        return new PostTransportOffer(1, Date.valueOf("2022-3-28"), Date.valueOf("2022-3-28"), 29.03f);
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

    /** GET - get all transport offers **/
    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenFetchOffers_thenStatus200()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();
        ResultActions resultActions = mvc.perform(get("/transport-offers"));


        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "[{\"transportOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"startDate\":\"2022-03-27T22:00:00.000+00:00\",\"endDate\":\"2022-03-27T22:00:00.000+00:00\",\"price\":29.03}]";
        assertEquals(expected, result);

        offerService.deleteTransportOffer(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "-", password = "-")
    public void givenUserWithoutPermission_whenFetchOffers_thenStatus403()
            throws Exception {
        mvc.perform(get("/transport-offers")).andExpect(status().isForbidden());

    }


    /** GET - get order by ID **/

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenFetchOfferById_thenStatus200()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();
        ResultActions resultActions = mvc.perform(get("/transport-offers/"+ id));


        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "{\"transportOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"startDate\":\"2022-03-27T22:00:00.000+00:00\",\"endDate\":\"2022-03-27T22:00:00.000+00:00\",\"price\":29.03}";
        assertEquals(expected, result);

        offerService.deleteTransportOffer(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenFetchOfferById_thenStatus403()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();
        ResultActions resultActions = mvc.perform(get("/transport-offers/"+ id));


        resultActions.andExpect(status().isForbidden());

        offerService.deleteTransportOffer(id);
    }


    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenFetchOfferById_thenStatus403()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();
        ResultActions resultActions = mvc.perform(get("/transport-offers/"+ id));


        resultActions.andExpect(status().isForbidden());

        offerService.deleteTransportOffer(id);
    }

    /** GET - transport offers by carrier ID **/


    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchCarrierOffers_thenStatus200()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();
        ResultActions resultActions = mvc.perform(get("/carriers/3/transport-offers"));


        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        String expected = "[{\"transportOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"startDate\":\"2022-03-27T22:00:00.000+00:00\",\"endDate\":\"2022-03-27T22:00:00.000+00:00\",\"price\":29.03}]";
        assertEquals(expected, result);

        offerService.deleteTransportOffer(id);
    }


    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenFetchCarrierOffers_thenStatus403()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();
        ResultActions resultActions = mvc.perform(get("/carriers/3/transport-offers"));

        resultActions.andExpect(status().isForbidden());

        offerService.deleteTransportOffer(id);
    }

    /** POST - create offer **/


    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenCreateOffer_thenStatus200()
            throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/carriers/3/transport-offers").
                                contentType(MediaType.APPLICATION_JSON).content(convertToJSON(generateTransportOffer())));

        resultActions.andExpect(status().isCreated());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        long id = offerService.getAllTransportOffers().body().stream().findFirst().get().getTransportOfferID();


        String expected = "{\"transportOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"startDate\":\"2022-03-27T22:00:00.000+00:00\",\"endDate\":\"2022-03-27T22:00:00.000+00:00\",\"price\":29.03}";

        assertEquals(expected, result);

        offerService.deleteTransportOffer(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenCreateNotValidOffer_thenStatus403()
            throws Exception {

        PostTransportOffer postTransportOffer = new PostTransportOffer(1, Date.valueOf("2022-3-28"), Date.valueOf("2022-3-28"), -29.03f);

        ResultActions resultActions =
                mvc.perform(
                        post("/carriers/3/transport-offers").
                                contentType(MediaType.APPLICATION_JSON).content(convertToJSON(postTransportOffer)));

        resultActions.andExpect(status().isBadRequest());


    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenCreateOffer_thenStatus403()
            throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/carriers/3/transport-offers").
                                contentType(MediaType.APPLICATION_JSON).content(convertToJSON(generateTransportOffer())));

        resultActions.andExpect(status().isForbidden());

    }

    /** DELETE - delete offer by id **/

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenDeleteOfferById_thenStatus204()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();

        ResultActions resultActions = mvc.perform(delete("/transport-offers/"+ id));


        resultActions.andExpect(status().isNoContent());
    }

    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenDeleteOfferById_thenStatus403()
            throws Exception {

        long id = offerService.createTransportOffer(generateTransportOffer(), 3).body().getTransportOfferID();

        ResultActions resultActions = mvc.perform(delete("/transport-offers/"+ id));


        resultActions.andExpect(status().isForbidden());
    }

}
