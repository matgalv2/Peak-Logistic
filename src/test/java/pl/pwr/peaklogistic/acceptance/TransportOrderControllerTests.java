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
public class TransportOrderControllerTests {

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

    /** GET - get all transport orders **/
    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchOrders_thenStatus200()
            throws Exception {

        long id = orderService.createTransportOrder(generateOrder(),2).body().getTransportOrderID();
        ResultActions resultActions = mvc.perform(get("/transport-orders"));


        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "[{\"transportOrderID\":"+id+",\"customer\":{\"userID\":2,\"email\":\"customer@customer.org\",\"nickname\":\"nickname\"},\"completed\":false,\"fromLocation\":\"a\",\"toLocation\":\"b\",\"startDateFrom\":\"2022-03-28\",\"startDateTo\":\"2022-03-28\",\"endDateFrom\":\"2022-03-28\",\"endDateTo\":\"2022-03-28\",\"productWeightKG\":21.3,\"productHeightCM\":100,\"productWidthCM\":100,\"productDepthCM\":100,\"transportOffers\":[]}]";

        assertEquals(expected, result);

        orderService.deleteTransportOrder(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenFetchOrders_thenStatus403()
            throws Exception {

        ResultActions resultActions = mvc.perform(get("/transport-orders"));

        resultActions.andExpect(status().isForbidden());
    }

    /** GET - get order by ID **/

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchOrderByID_thenStatus200()
            throws Exception {

        long id = orderService.createTransportOrder(generateOrder(),2).body().getTransportOrderID();
        ResultActions resultActions = mvc.perform(get("/transport-orders/" + id));


        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "{\"transportOrderID\":"+ id + ",\"customer\":{\"userID\":2,\"email\":\"customer@customer.org\",\"nickname\":\"nickname\"},\"completed\":false,\"fromLocation\":\"a\",\"toLocation\":\"b\",\"startDateFrom\":\"2022-03-28\",\"startDateTo\":\"2022-03-28\",\"endDateFrom\":\"2022-03-28\",\"endDateTo\":\"2022-03-28\",\"productWeightKG\":21.3,\"productHeightCM\":100,\"productWidthCM\":100,\"productDepthCM\":100,\"transportOffers\":[]}";

        assertEquals(expected, result);

        orderService.deleteTransportOrder(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchNonexistentOrderById_thenStatus404()
            throws Exception {

        ResultActions resultActions = mvc.perform(get("/transport-orders/100"));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenFetchOrderById_thenStatus403()
            throws Exception {


        ResultActions resultActions = mvc.perform(get("/transport-orders/1"));

        resultActions.andExpect(status().isForbidden());
    }

    /** GET - fetch customer's orders **/

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenFetchCustomerOrders_thenStatus200() throws Exception {

        long id = orderService.createTransportOrder(generateOrder(),2).body().getTransportOrderID();
        ResultActions resultActions = mvc.perform(get("/customers/2/transport-orders"));

        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        String expected = "[{\"transportOrderID\":"+ id + ",\"customer\":{\"userID\":2,\"email\":\"customer@customer.org\",\"nickname\":\"nickname\"},\"completed\":false,\"fromLocation\":\"a\",\"toLocation\":\"b\",\"startDateFrom\":\"2022-03-28\",\"startDateTo\":\"2022-03-28\",\"endDateFrom\":\"2022-03-28\",\"endDateTo\":\"2022-03-28\",\"productWeightKG\":21.3,\"productHeightCM\":100,\"productWidthCM\":100,\"productDepthCM\":100,\"transportOffers\":[]}]";

        assertEquals(expected, result);

        orderService.deleteTransportOrder(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "-", password = "-")
    public void givenUserWithoutPermission_whenFetchCustomerOrders_thenStatus403()
            throws Exception {


        ResultActions resultActions = mvc.perform(get("/customers/2/transport-orders"));

        resultActions.andExpect(status().isForbidden());
    }

    /** GET - fetch carrier's orders, where he added offer **/

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchCarrierOrders_thenStatus200() throws Exception {




        long id = orderService.createTransportOrder(generateOrder(),2).body().getTransportOrderID();
        offerService.createTransportOffer(new PostTransportOffer(id, Date.valueOf("2022-3-28"), Date.valueOf("2022-3-28"), 14.45f), 3);
        ResultActions resultActions = mvc.perform(get("/carriers/3/transport-orders"));

        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        String expected = "[{\"transportOrderID\":" + id + ",\"customer\":{\"userID\":2,\"email\":\"customer@customer.org\",\"nickname\":\"nickname\"},\"completed\":false,\"fromLocation\":\"a\",\"toLocation\":\"b\",\"startDateFrom\":\"2022-03-28\",\"startDateTo\":\"2022-03-28\",\"endDateFrom\":\"2022-03-28\",\"endDateTo\":\"2022-03-28\",\"productWeightKG\":21.3,\"productHeightCM\":100,\"productWidthCM\":100,\"productDepthCM\":100,\"transportOffers\":[{\"transportOfferID\":1,\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"startDate\":\"2022-03-27T22:00:00.000+00:00\",\"endDate\":\"2022-03-27T22:00:00.000+00:00\",\"price\":14.45}]}]";

        assertEquals(expected, result);

        orderService.deleteTransportOrder(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "-", password = "-")
    public void givenUserWithoutPermission_whenFetchCarrierOrders_thenStatus403()
            throws Exception {


        ResultActions resultActions = mvc.perform(get("/carriers/3/transport-orders"));

        resultActions.andExpect(status().isForbidden());
    }


    /** POST - creating transport order **/

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenCreateOrder_thenStatus200()
            throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/customers/2/transport-orders").
                        contentType(MediaType.APPLICATION_JSON).content(convertToJSON(generateOrder())));

        resultActions.andExpect(status().isCreated());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        long id = orderService.getAllTransportOrders().body().stream().findFirst().get().getTransportOrderID();


        String expected = "{\"transportOrderID\":"+id+",\"customer\":{\"userID\":2,\"email\":\"customer@customer.org\",\"nickname\":\"nickname\"},\"completed\":false,\"fromLocation\":\"a\",\"toLocation\":\"b\",\"startDateFrom\":\"2022-03-28\",\"startDateTo\":\"2022-03-28\",\"endDateFrom\":\"2022-03-28\",\"endDateTo\":\"2022-03-28\",\"productWeightKG\":21.3,\"productHeightCM\":100,\"productWidthCM\":100,\"productDepthCM\":100,\"transportOffers\":null}";

        assertEquals(expected, result);

        orderService.deleteTransportOrder(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithoutPermission_whenCreateOrder_thenStatus403()
            throws Exception {

        ResultActions resultActions = mvc.perform(post("/customers/2/transport-orders"));

        resultActions.andExpect(status().isForbidden());
    }

    /** PATCH - set order as completed **/


    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenSetOrderAsCompleted_thenStatus204()
            throws Exception {

        long id = orderService.createTransportOrder(generateOrder(),2).body().getTransportOrderID();

        ResultActions resultActions = mvc.perform(patch("/transport-orders/"+ id));

        resultActions.andExpect(status().isNoContent());

        orderService.deleteTransportOrder(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithoutPermission_whenSetOrderAsCompleted_thenStatus403() throws Exception {

        mvc.perform(patch("/transport-orders/1")).andExpect(status().isForbidden());

    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenSetNonexistentOrderAsCompleted_thenStatus404() throws Exception {
        // deadline is very close
        mvc.perform(patch("/transport-orders/9999")).andExpect(status().isForbidden());

    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenSetCompletedOrderAsCompleted_thenStatus400()
            throws Exception {

        PostTransportOrder postTransportOrder = generateOrder();
        postTransportOrder.setCompleted(true);
        long id = orderService.createTransportOrder(postTransportOrder,2).body().getTransportOrderID();

        ResultActions resultActions = mvc.perform(patch("/transport-orders/"+ id));

        resultActions.andExpect(status().isBadRequest());

        orderService.deleteTransportOrder(id);
    }

    /** DELETE - delete order by id**/

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenDeleteOrder_thenStatus204()
            throws Exception {

        long id = orderService.createTransportOrder(generateOrder(),2).body().getTransportOrderID();

        ResultActions resultActions = mvc.perform(delete("/transport-orders/"+ id));

        resultActions.andExpect(status().isNoContent());

    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithoutPermission_whenDeleteOrder_thenStatus403() throws Exception {

        mvc.perform(delete("/transport-orders/1")).andExpect(status().isForbidden());
    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenDeleteNonexistentOrder_thenStatus404() throws Exception {
        // deadline is very close
        mvc.perform(delete("/transport-orders/9999")).andExpect(status().isForbidden());
    }


}
