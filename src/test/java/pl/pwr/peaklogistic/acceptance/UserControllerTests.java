package pl.pwr.peaklogistic.acceptance;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pwr.peaklogistic.PeakLogisticApplication;
import pl.pwr.peaklogistic.controller.UserController;
import pl.pwr.peaklogistic.repository.UserRepository;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PeakLogisticApplication.class)
@AutoConfigureMockMvc
//@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin@admin@eu", password = "pass1")
    public void givenEmployees_whenGetEmployees_thenStatus200()
            throws Exception {

        mvc.perform(get("/users")).andExpect(status().isOk())




//        mvc
//                .perform(
//                post("/login")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//                        .content("username=admin@admin.eu&password=pass1")
//                )
//                .andExpect(status().isOk())
//                .andDo(
//                        x -> {
//                            log.error(x.getResponse().getContentAsString());
//
//                        }
//                );

;
//        mvc.perform(get("/api/employees")
//                        .contentType(MediaType.APPLICATION_JSON.toString()))
//                .andExpect(status().isOk())
////                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].name", is("bob")));
    }

}
