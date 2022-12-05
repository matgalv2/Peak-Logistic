package pl.pwr.peaklogistic.user;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersRepositoryTests {

    UserRepository userRepository;


    private void cleanTable(){
        userRepository.deleteAll(userRepository.findAll());
    }

    private @Valid User generateUser(){
        User user = new User();
        user.setUserID(1L);
        user.setUserType(UserType.Customer);
        user.setEmail("customer@me.eu");
        user.setPassword(UUID.randomUUID().toString());
        return user;
    }

    @Test
    @DisplayName("Succeeds to fetch all users")
    @Rollback
    void fetchAll(){
        cleanTable();
        var result = userRepository.findAll();

        assertEquals(result, List.of());
    }

    @Test
    @DisplayName("Succeeds to fetch user by id")
    @Rollback
    void fetchById(){
        User gen = generateUser();
        userRepository.save(gen);

        var result = userRepository.findById(gen.getUserID());
        assertTrue(result.isPresent());
    }



}
