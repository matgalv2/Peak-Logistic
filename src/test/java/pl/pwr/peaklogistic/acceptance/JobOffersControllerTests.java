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
import pl.pwr.peaklogistic.dto.request.user.*;
import pl.pwr.peaklogistic.services.JobOfferService;
import pl.pwr.peaklogistic.services.UserService;


import java.io.IOException;

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
public class JobOffersControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserService userService;
    @Autowired
    private JobOfferService jobOfferService;



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


    /** GET - get all job offers**/

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchJobOffers_thenStatus200()
            throws Exception {

        long id = jobOfferService.createJobOffer(generateJobOffer(), 3).body().getJobOfferID();
        ResultActions resultActions = mvc.perform(get("/job-offers"));

        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "[{\"jobOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"titlePL\":\"tytul\",\"titleENG\":\"title\",\"contentPL\":\"tresc\",\"contentENG\":\"content\",\"contactEmail\":\"contact@email.com\"}]";

        assertEquals(expected, result);

        jobOfferService.deleteJobOffer(id);
    }


    /** GET - get job offer by ID **/

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchJobOfferByID_thenStatus200()
            throws Exception {

        long id = jobOfferService.createJobOffer(generateJobOffer(), 3).body().getJobOfferID();
        ResultActions resultActions = mvc.perform(get("/job-offers/" + id));


        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();


        String expected = "{\"jobOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"titlePL\":\"tytul\",\"titleENG\":\"title\",\"contentPL\":\"tresc\",\"contentENG\":\"content\",\"contactEmail\":\"contact@email.com\"}";

        assertEquals(expected, result);

        jobOfferService.deleteJobOffer(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenFetchNonexistentJobOfferByID_thenStatus200()
            throws Exception {

        long id = jobOfferService.createJobOffer(generateJobOffer(), 3).body().getJobOfferID();
        mvc.perform(get("/job-offers/9999")).andExpect(status().isNotFound());
        jobOfferService.deleteJobOffer(id);
    }

    /** POST - create job offer **/

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenCreateOrder_thenStatus200()
            throws Exception {

        ResultActions resultActions =
                mvc.perform(
                        post("/carriers/3/job-offers").
                                contentType(MediaType.APPLICATION_JSON).content(convertToJSON(generateJobOffer())));

        resultActions.andExpect(status().isCreated());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        long id = jobOfferService.getAllJobOffers().body().stream().findFirst().get().getJobOfferID();


        String expected = "{\"jobOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"titlePL\":\"tytul\",\"titleENG\":\"title\",\"contentPL\":\"tresc\",\"contentENG\":\"content\",\"contactEmail\":\"contact@email.com\"}";

        assertEquals(expected, result);

        jobOfferService.deleteJobOffer(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "-@carrier.org", password = "-", authorities = "CARRIER")
    public void givenUserWithoutPermission_whenCreateOrder_thenStatus403()
            throws Exception {

        mvc.perform(post("/carriers/3/job-offers")).andExpect(status().isForbidden());
    }

    /** PUT - update job offer**/

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenUpdateJobOffer_thenStatus200()
            throws Exception {

        long id = jobOfferService.createJobOffer(generateJobOffer(), 3).body().getJobOfferID();

        PutJobOffer putJobOffer = new PutJobOffer("tytul2", "title2", "tresc2", "content2", "contact2@email.com");
        MockHttpServletRequestBuilder request =
                put("/job-offers/"+ id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putJobOffer));

        ResultActions resultActions = mvc.perform(request).andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        String expected = "{\"jobOfferID\":"+id+",\"carrier\":{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"},\"titlePL\":\"tytul2\",\"titleENG\":\"title2\",\"contentPL\":\"tresc2\",\"contentENG\":\"content2\",\"contactEmail\":\"contact2@email.com\"}";
        assertEquals(expected, result);
        jobOfferService.deleteJobOffer(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "-", password = "-")
    public void givenUserWithoutPermission_whenUpdateJobOffer_thenStatus200()
            throws Exception {

        long id = jobOfferService.createJobOffer(generateJobOffer(), 3).body().getJobOfferID();

        PutJobOffer putJobOffer = new PutJobOffer("tytul2", "title2", "tresc2", "content2", "contact2@email.com");
        MockHttpServletRequestBuilder request =
                put("/job-offers/"+ id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putJobOffer));

        mvc.perform(request).andExpect(status().isForbidden());

        jobOfferService.deleteJobOffer(id);
    }

    /** DELETE - delete job offer by id**/


    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenDeleteJobOfferByID_thenStatus200()
            throws Exception {

        long id = jobOfferService.createJobOffer(generateJobOffer(), 3).body().getJobOfferID();
        mvc.perform(delete("/job-offers/" + id)).andExpect(status().isNoContent());

        jobOfferService.deleteJobOffer(id);
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenDeleteJobOfferByID_thenStatus200()
            throws Exception {

        long id = jobOfferService.createJobOffer(generateJobOffer(), 3).body().getJobOfferID();
        mvc.perform(delete("/job-offers/" + id)).andExpect(status().isForbidden());
        jobOfferService.deleteJobOffer(id);
    }


}
