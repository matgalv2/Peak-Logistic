//package pl.pwr.peaklogistic.user;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.ClassRule;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.util.TestPropertyValues;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.testcontainers.containers.MySQLContainer;
//import pl.pwr.peaklogistic.common.UserType;
//import pl.pwr.peaklogistic.model.User;
//import pl.pwr.peaklogistic.repository.UserRepository;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(initializers = {UsersRepositoryTCIntegrationTests.Initializer.class})
//@Slf4j
//public class UsersRepositoryTCIntegrationTests  {
//
//    UserRepository userRepository;
//
//    @ClassRule
//    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:oracle")
//            .withDatabaseName("integration-tests-db")
//            .withUsername("root")
//            .withPassword("pass");
//
//    static class Initializer
//            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            log.error("spring.datasource.url=" + mySQLContainer.getJdbcUrl());
//            TestPropertyValues.of(
//                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
//                    "spring.datasource.username=" + mySQLContainer.getUsername(),
//                    "spring.datasource.password=" + mySQLContainer.getPassword()
//            ).applyTo(configurableApplicationContext.getEnvironment());
//        }
//    }
//
//
//    private void cleanTable(){
//        userRepository.deleteAll(userRepository.findAll());
//    }
//
//    private User generateUser(){
//        User user = new User();
//        user.setUserID(1L);
//        user.setUserType(UserType.Customer);
//        user.setEmail("customer@me.eu");
//        user.setPassword(UUID.randomUUID().toString());
//        return user;
//    }
//
////    @Test
////    @DisplayName("Succeeds to fetch all users")
////    @Rollback
////    void fetchAll(){
//////        cleanTable();
////        var result = userRepository.findAll();
////
////
////        assertEquals(result, List.of());
////    }
//
////    @Test
////    @DisplayName("Succeeds to fetch user by id")
////    @Rollback
////    void fetchById(){
////        User gen = generateUser();
////        userRepository.save(gen);
////
////        var result = userRepository.findById(gen.getUserID());
////        assertTrue(result.isPresent());
////    }
//
//
//
//}
