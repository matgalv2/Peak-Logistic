package pl.pwr.peaklogistic.integration.transportOrder;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.peaklogistic.PeakLogisticApplication;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = PeakLogisticApplication.class)
public class TransportOrderRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransportOrderRepository orderRepository;

    private User createCustomer(){
        User user = new User();
        user.setUserType(UserType.Customer);
        user.setPassword("password");
        user.setEmail("email@op.gg");
        user.setNickname("trebor7");

        return user;
    }

    private TransportOrder generateTransportOrder(User customer){
        TransportOrder order = new TransportOrder();
        order.setCustomer(customer);
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


    @Test
    @Rollback
    @Transactional
    public void getTransportOrderById(){

        User user = createCustomer();
        User addedUser = userRepository.save(user);

        TransportOrder order = orderRepository.save(generateTransportOrder(addedUser));

        var result = orderRepository.findById(order.getTransportOrderID());

        assertTrue(result.isPresent());
    }

    @Test
    @Rollback
    @Transactional
    public void getAllTransportOrders(){
        User user = createCustomer();
        User addedUser = userRepository.save(user);

        TransportOrder order1 = generateTransportOrder(addedUser);
        TransportOrder order2 = generateTransportOrder(addedUser);

        var expectedOrder1 = orderRepository.save(order1);
        var expectedOrder2 = orderRepository.save(order2);

        var result = orderRepository.findAll();

        assertEquals(List.of(expectedOrder1, expectedOrder2), result);
    }

    @Test
    @Rollback
    @Transactional
    public void createTransportOrder(){
        User user = createCustomer();
        User addedUser = userRepository.save(user);

        TransportOrder order = orderRepository.save(generateTransportOrder(addedUser));

        var result = orderRepository.findById(order.getTransportOrderID());

        assertTrue(result.isPresent());
    }

    @Test
    @Rollback
    @Transactional
    public void updateTransportOrderStatus(){
        User user = createCustomer();
        User addedUser = userRepository.save(user);

        TransportOrder order = orderRepository.save(generateTransportOrder(addedUser));

        var afterAdd = orderRepository.findById(order.getTransportOrderID());



        if(afterAdd.isEmpty())
            fail("Couldn't fetch transport order");

        TransportOrder orderAfterAdd = afterAdd.get();
        boolean statusAfterAdd = orderAfterAdd.isCompleted();
        orderAfterAdd.setCompleted(true);

        TransportOrder afterUpdate = orderRepository.save(orderAfterAdd);

        var result = orderRepository.findById(order.getTransportOrderID());

        if(result.isEmpty())
            fail("Couldn't fetch transport order");

        assertTrue(statusAfterAdd != result.get().isCompleted());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteTransportOrder(){
        User user = createCustomer();
        User addedUser = userRepository.save(user);

        TransportOrder order = orderRepository.save(generateTransportOrder(addedUser));

        var beforeDelete = orderRepository.findById(order.getTransportOrderID());
        orderRepository.deleteById(order.getTransportOrderID());
        var afterDelete = orderRepository.findById(order.getTransportOrderID());

        assertTrue(beforeDelete.isPresent() && afterDelete.isEmpty());
    }

}
