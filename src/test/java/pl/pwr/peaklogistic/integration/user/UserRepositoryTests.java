package pl.pwr.peaklogistic.integration.user;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pwr.peaklogistic.PeakLogisticApplication;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.UserRepository;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = PeakLogisticApplication.class)
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    private User createCarrier()
    {
        User user = new User();
        user.setUserType(UserType.Carrier);
        user.setPassword("password");
        user.setEmail("peaklogistics@me.eu");
        user.setCompanyName("transportex");
        user.setTaxIdentificationNumber("1234567890");
        user.setPhone("+1 345678123");

        return user;
    }

    private User createCustomer(){
        User user = new User();
        user.setUserType(UserType.Customer);
        user.setPassword("password");
        user.setEmail("email@op.gg");
        user.setNickname("trebor7");

        return user;
    }

    private User createAdmin(){
        User user = new User();
        user.setUserType(UserType.Admin);
        user.setPassword("password");
        user.setEmail("trebor7@o2.pl");

        return user;
    }

    @Test
    public void getUserById(){
        User user = createCustomer();

        User expectedUser = userRepository.save(user);
        Optional<User> result = userRepository.findById(expectedUser.getUserID());

        assertEquals(Optional.of(expectedUser), result);
    }

    @Test
    public void getAllUsers(){
        User user = createAdmin();
        User user2 = createCarrier();

        User expectedUser1 = userRepository.save(user);
        User expectedUser2 = userRepository.save(user2);

        List<User> result = userRepository.findAll();

        assertEquals(List.of(expectedUser1, expectedUser2), result);
    }

    @Test
    public void createUser(){
        User admin = createAdmin();
        User addedUser = userRepository.save(admin);
        Optional<User> result = userRepository.findById(addedUser.getUserID());

        assertTrue(result.isPresent());
    }


    @Test
    public void updateUser(){
        User admin = createAdmin();
        admin = userRepository.save(admin);
        String oldNickname = admin.getNickname();
        Optional<User> found = userRepository.findById(admin.getUserID());

        if (found.isEmpty())
            fail("User was not found");
        else {
            found.get().setNickname("new nickname");
            userRepository.save(found.get());
            Optional<User> afterUpdate = userRepository.findById(admin.getUserID());
            assertEquals(admin.getUserID(), afterUpdate.get().getUserID());
            assertNotEquals(oldNickname, afterUpdate.get().getNickname());
        }
    }


    @Test
    public void deleteUser(){
        User admin = createAdmin();

        long usersInDB = userRepository.count();

        User saved = userRepository.save(admin);

        long usersInDB_afterAdd = userRepository.count();

        userRepository.deleteById(saved.getUserID());

        Optional<User> afterDel = userRepository.findById(saved.getUserID());

        assertTrue(afterDel.isEmpty() && usersInDB != usersInDB_afterAdd);
    }



}
