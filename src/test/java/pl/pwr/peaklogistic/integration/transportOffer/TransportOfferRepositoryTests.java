package pl.pwr.peaklogistic.integration.transportOffer;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.peaklogistic.PeakLogisticApplication;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.TransportOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.TransportOfferRepository;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = PeakLogisticApplication.class)
public class TransportOfferRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransportOrderRepository orderRepository;

    @Autowired
    TransportOfferRepository offerRepository;

    private User generateCustomer(){
        User user = new User();
        user.setUserType(UserType.Customer);
        user.setPassword("password");
        user.setEmail("email@op.gg");
        user.setNickname("trebor7");

        return user;
    }

    private User generateCarrier()
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

    private TransportOffer generateTransportOffer(User carrier, long orderID){
        TransportOffer offer = new TransportOffer();
        offer.setCarrier(carrier);
        offer.setTransportOrderID(orderID);
        offer.setStartDate(Date.valueOf("2022-12-10"));
        offer.setEndDate(Date.valueOf("2022-12-11"));
        offer.setPrice(34.30f);

        return offer;
    }


    @Test
    @Rollback
    @Transactional
    public void getTransportOfferById(){
        User customer = generateCustomer();
        User addedCustomer = userRepository.save(customer);
        TransportOrder order = orderRepository.save(generateTransportOrder(addedCustomer));

        User carrier = generateCustomer();
        User addedCarrier = userRepository.save(carrier);
        TransportOffer offer = offerRepository.save(generateTransportOffer(addedCarrier, order.getTransportOrderID()));

        var result = offerRepository.findById(offer.getTransportOfferID());

        assertTrue(result.isPresent());

    }

    @Test
    @Rollback
    @Transactional
    public void getAllTransportOffers(){
        User customer = generateCustomer();
        User addedCustomer = userRepository.save(customer);

        TransportOrder order = orderRepository.save(generateTransportOrder(addedCustomer));

        User addedCarrier = userRepository.save(generateCarrier());
        TransportOffer offer = offerRepository.save(generateTransportOffer(addedCarrier, order.getTransportOrderID()));
        TransportOffer offer2 = offerRepository.save(generateTransportOffer(addedCarrier, order.getTransportOrderID()));


        var result = offerRepository.findAll();
        assertEquals(List.of(offer, offer2), result);


    }

    @Test
    @Rollback
    @Transactional
    public void createTransportOffer(){
        User customer = generateCustomer();
        User addedCustomer = userRepository.save(customer);
        TransportOrder order = orderRepository.save(generateTransportOrder(addedCustomer));

        User carrier = generateCustomer();
        User addedCarrier = userRepository.save(carrier);
        TransportOffer offer = offerRepository.save(generateTransportOffer(addedCarrier, order.getTransportOrderID()));

        var result = offerRepository.findById(offer.getTransportOfferID());

        assertTrue(result.isPresent());
    }

    @Test
    @Rollback
    @Transactional
    public void updateTransportOffer(){

        User customer = generateCustomer();
        User addedCustomer = userRepository.save(customer);
        TransportOrder order = orderRepository.save(generateTransportOrder(addedCustomer));

        User carrier = generateCustomer();
        User addedCarrier = userRepository.save(carrier);
        TransportOffer offer = offerRepository.save(generateTransportOffer(addedCarrier, order.getTransportOrderID()));

        var beforeUpdate = offerRepository.findById(offer.getTransportOfferID());

        if(beforeUpdate.isEmpty())
            fail("Couldn't fetch transport offer");

        float priceBeforeUpdate = beforeUpdate.get().getPrice();

        var afterUpdate = beforeUpdate.get();

        afterUpdate.setPrice(1.11f);

        TransportOffer offerAfterUpdate = offerRepository.save(afterUpdate);

        var result = offerRepository.findById(afterUpdate.getTransportOfferID());

        if(result.isEmpty())
            fail("Couldn't fetch transport offer");

        float priceAfterUpdate = result.get().getPrice();

        assertTrue(priceBeforeUpdate != priceAfterUpdate);

    }

    @Test
    @Rollback
    @Transactional
    public void deleteTransportOffer(){

        User customer = generateCustomer();
        User addedCustomer = userRepository.save(customer);
        TransportOrder order = orderRepository.save(generateTransportOrder(addedCustomer));

        User carrier = generateCustomer();
        User addedCarrier = userRepository.save(carrier);
        TransportOffer offer = offerRepository.save(generateTransportOffer(addedCarrier, order.getTransportOrderID()));

        var beforeDelete = offerRepository.findById(offer.getTransportOfferID());

        offerRepository.deleteById(offer.getTransportOfferID());

        var afterDelete = offerRepository.findById(offer.getTransportOfferID());

        assertTrue(beforeDelete.isPresent() && afterDelete.isEmpty());

    }
}
