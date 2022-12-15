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
import pl.pwr.peaklogistic.dto.request.user.*;
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
public class UserControllerTests {

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
    }

    //        mvc.perform(get("/api/employees")
//                        .contentType(MediaType.APPLICATION_JSON.toString()))
//                .andExpect(status().isOk())
////                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].name", is("bob")));

    private void createUsers(){
        PostUser postUser = new PostUser("admin@admin.org", "pass1");
        userService.createAdmin(postUser);

        PostCustomer postCustomer = new PostCustomer("customer@customer.org", "pass1", "nickname");
        userService.createCustomer(postCustomer);

        PostCarrier postCarrier = new PostCarrier("carrier@carrier.org", "pass1", "transportex", "+45 345678789", "1234567890");
        userService.createCarrier(postCarrier);
    }

    private String convertToJSONString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

    private byte[] convertToJSON(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenAdmin_whenFetchUsers_thenStatus200()
            throws Exception {

        createUsers();

        ResultActions resultActions = mvc.perform(get("/users"));


        resultActions.andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        String expected = "[{\"userID\":1,\"email\":\"admin@admin.org\",\"userType\":\"Admin\",\"nickname\":null,\"companyName\":null,\"phone\":null,\"taxIdentificationNumber\":null}," +
                "{\"userID\":2,\"email\":\"customer@customer.org\",\"userType\":\"Customer\",\"nickname\":\"nickname\",\"companyName\":null,\"phone\":null,\"taxIdentificationNumber\":null}," +
                "{\"userID\":3,\"email\":\"carrier@carrier.org\",\"userType\":\"Carrier\",\"nickname\":null,\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"}]";
        assertEquals(expected, result);
    }

    @Test
    @Rollback
    @WithMockUser(username = "", password = "")
    public void givenAnyUserExceptAdmin_whenFetchUsers_thenStatus403()
            throws Exception {

        createUsers();

        mvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenAdmin_whenFetchUser_thenStatus200()
            throws Exception {

        createUsers();

        ResultActions resultActions = mvc.perform(get("/users/2"));

        String result = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().isOk());
        String expected = "{\"userID\":2,\"email\":\"customer@customer.org\",\"userType\":\"Customer\",\"nickname\":\"nickname\",\"companyName\":null,\"phone\":null,\"taxIdentificationNumber\":null}";
        assertEquals(expected, result);
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUser_whenFetchHimself_thenStatus200()
            throws Exception {

        createUsers();

        ResultActions resultActions = mvc.perform(get("/users/2"));

        String result = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().isOk());
        String expected = "{\"userID\":2,\"email\":\"customer@customer.org\",\"userType\":\"Customer\",\"nickname\":\"nickname\",\"companyName\":null,\"phone\":null,\"taxIdentificationNumber\":null}";
        assertEquals(expected, result);
    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenAdmin_whenFetchNonexistentUser_thenStatus400()
            throws Exception {

        createUsers();

        mvc.perform(get("/users/100")).andExpect(status().isNotFound());
    }


    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenAdmin_whenFetchCustomers_thenStatus200()
            throws Exception {

        createUsers();

        ResultActions resultActions = mvc.perform(get("/customers"));

        String result = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().isOk());
        String expected = "[{\"userID\":2,\"email\":\"customer@customer.org\",\"nickname\":\"nickname\"}]";
        assertEquals(expected, result);
    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenAnyUserExceptAdmin_whenFetchCustomers_thenStatus403()
            throws Exception {

        createUsers();
        mvc.perform(get("/customers")).andExpect(status().isForbidden());
    }


    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenAdmin_whenFetchCarriers_thenStatus200()
            throws Exception {

        createUsers();

        ResultActions resultActions = mvc.perform(get("/carriers"));

        String result = resultActions.andReturn().getResponse().getContentAsString();

        resultActions.andExpect(status().isOk());
        String expected = "[{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"transportex\",\"phone\":\"+45 345678789\",\"taxIdentificationNumber\":\"1234567890\"}]";
        assertEquals(expected, result);
    }

    @Test
    @Rollback
    @WithMockUser(username = "-", password ="-")
    public void givenAnyUserExceptLoggedOnes_whenFetchCarriers_thenStatus403()
            throws Exception {

        createUsers();
        mvc.perform(get("/customers")).andExpect(status().isForbidden());

//        log.error(convertToJSON(new UserPasswordRequest("12345", "12345", "12345")));
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenCustomer_whenChangePwd_thenStatus204()
            throws Exception {

        createUsers();

        String pwdBeforeUpdate = userService.getUserById(2).body().getPassword();
        UserPasswordRequest pwdReq = new UserPasswordRequest("pass1", "12345", "12345");
        MockHttpServletRequestBuilder request =
                put("/users/2/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(pwdReq));

        mvc.perform(request).andExpect(status().isNoContent());

        String pwdAfterUpdate = userService.getUserById(2).body().getPassword();

        assertNotEquals(pwdBeforeUpdate, pwdAfterUpdate);

        UserPasswordRequest pwdReqAfterTest = new UserPasswordRequest("12345", "pass1", "pass1");
        mvc.perform(put("/users/2/pwd").contentType(MediaType.APPLICATION_JSON).content(convertToJSON(pwdReqAfterTest)));
    }


    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenAdmin_whenChangeUserPwd_thenStatus204()
            throws Exception {

        createUsers();

        String pwdBeforeUpdate = userService.getUserById(2).body().getPassword();
        UserPasswordRequest pwdReq = new UserPasswordRequest("pass1", "23456", "23456");
        MockHttpServletRequestBuilder request =
                put("/users/2/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(pwdReq));

        mvc.perform(request).andExpect(status().isNoContent());

        String pwdAfterUpdate = userService.getUserById(2).body().getPassword();

        assertNotEquals(pwdBeforeUpdate, pwdAfterUpdate);

        UserPasswordRequest pwdReqAfterTest = new UserPasswordRequest("12345", "pass1", "pass1");
        mvc.perform(put("/users/2/pwd").contentType(MediaType.APPLICATION_JSON).content(convertToJSON(pwdReqAfterTest)));
    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenAdmin_whenChangeNonexistentUserPwd_thenStatus404()
            throws Exception {

        createUsers();

        String pwdBeforeUpdate = userService.getUserById(2).body().getPassword();
        UserPasswordRequest pwdReq = new UserPasswordRequest("pass1", "23456", "23456");
        MockHttpServletRequestBuilder request =
                put("/users/100/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(pwdReq));

        mvc.perform(request).andExpect(status().isNotFound());

    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenAnyUserExceptAdminAndPointedUser_whenChangeUserPwd_thenStatus403()
            throws Exception {

        createUsers();

        UserPasswordRequest pwdReq = new UserPasswordRequest("pass1", "12345", "12345");
        MockHttpServletRequestBuilder request =
                put("/users/2/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(pwdReq));

        mvc.perform(request).andExpect(status().isForbidden());
    }

    /** PUT - customer update **/

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithPermission_whenUpdateCustomer_thenStatus204()
            throws Exception {

        createUsers();

//        ResultActions resultActions = mvc.perform(get("/users"));
//
//
//        resultActions.andExpect(status().isOk());
//
//        String result = resultActions.andReturn().getResponse().getContentAsString();

        PutCustomer putCustomer = new PutCustomer("newNickname");
        MockHttpServletRequestBuilder request =
                put("/customers/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCustomer));

        ResultActions resultActions = mvc.perform(request);
        resultActions.andExpect(status().isOk());
        String result = resultActions.andReturn().getResponse().getContentAsString();
        String expected = "{\"userID\":2,\"email\":\"customer@customer.org\",\"nickname\":\"newNickname\"}";
        assertEquals(expected, result);

        PutCustomer putCustomerAfterTest = new PutCustomer("nickname");
        mvc.perform(
                put("/customers/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCustomerAfterTest)));
    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenUpdateNonexistentCustomer_thenStatus204()
            throws Exception {

        createUsers();

        PutCustomer putCustomer = new PutCustomer("newNickname");
        MockHttpServletRequestBuilder request =
                put("/customers/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCustomer));

        mvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithoutPermission_whenUpdateHimself_thenStatus403()
            throws Exception {

        createUsers();

        PutCustomer putCustomer = new PutCustomer("newNickname");
        MockHttpServletRequestBuilder request =
                put("/customers/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCustomer));

        mvc.perform(request).andExpect(status().isForbidden());
    }


    /** PUT - carrier update **/

    @Test
    @Rollback
    @WithMockUser(username = "carrier@carrier.org", password = "pass1", authorities = "CARRIER")
    public void givenUserWithPermission_whenUpdateCarrier_thenStatus200()
            throws Exception {

        createUsers();

        PutCarrier putCarrier = new PutCarrier("newCompanyName", "+34 536222111");
        MockHttpServletRequestBuilder request =
                put("/carriers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCarrier));

        ResultActions resultActions = mvc.perform(request);
        resultActions.andExpect(status().isOk());
        String result = resultActions.andReturn().getResponse().getContentAsString();
        String expected = "{\"userID\":3,\"email\":\"carrier@carrier.org\",\"companyName\":\"newCompanyName\",\"phone\":\"+34 536222111\",\"taxIdentificationNumber\":\"1234567890\"}";
        assertEquals(expected, result);



        PutCarrier putCarrierAfterTest = new PutCarrier("transportex", "+45 345678789");
        mvc.perform(
                put("/carriers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCarrierAfterTest)));
    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenUpdateNonexistentCarrier_thenStatus404()
            throws Exception {

        createUsers();

        PutCustomer putCustomer = new PutCustomer("newNickname");
        MockHttpServletRequestBuilder request =
                put("/carriers/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCustomer));

        mvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenUpdateCarrier_thenStatus403()
            throws Exception {

        createUsers();

        PutCustomer putCustomer = new PutCustomer("newNickname");
        MockHttpServletRequestBuilder request =
                put("/carriers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJSON(putCustomer));

        mvc.perform(request).andExpect(status().isForbidden());

    }

    /** DELETE - delete user **/


    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenDeleteUser_thenStatus204()
            throws Exception {

        createUsers();

        userService.createCustomer(new PostCustomer("customer2@customer.org", "pass1", "nickname"));

        MockHttpServletRequestBuilder request =
                delete("/users/4")
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isNoContent());

    }

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.org", password = "pass1", authorities = "ADMIN")
    public void givenUserWithPermission_whenDeleteNonexistentCarrier_thenStatus404()
            throws Exception {

        createUsers();

        mvc.perform(delete("/users/100").contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Rollback
    @WithMockUser(username = "customer@customer.org", password = "pass1", authorities = "CUSTOMER")
    public void givenUserWithoutPermission_whenDeleteUser_thenStatus403()
            throws Exception {

        createUsers();

        mvc.perform(delete("/users/1").contentType(MediaType.APPLICATION_JSON));

    }
}
