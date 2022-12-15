//package pl.pwr.peaklogistic;
//
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import pl.pwr.peaklogistic.common.UserType;
//import pl.pwr.peaklogistic.model.TransportOffer;
//import pl.pwr.peaklogistic.model.TransportOrder;
//import pl.pwr.peaklogistic.model.User;
//import pl.pwr.peaklogistic.repository.TransportOfferRepository;
//import pl.pwr.peaklogistic.repository.TransportOrderRepository;
//import pl.pwr.peaklogistic.repository.UserRepository;
//
//import java.sql.Date;
//
//
//@Component
//@AllArgsConstructor
//public class CMDRunner implements CommandLineRunner {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final TransportOrderRepository orderRepository;
//    private final TransportOfferRepository offerRepository;
//
//    @Override
//    public void run(String... args) {
//        if(userRepository.findAll().isEmpty()){
//            User user1 = new User();
//            user1.setEmail("admin@admin.org");
//            user1.setPassword(passwordEncoder.encode("pass1"));
//            user1.setUserType(UserType.Admin);
//            userRepository.save(user1);
//
//            User user2 = new User();
//            user2.setEmail("customer@customer.org");
//            user2.setPassword(passwordEncoder.encode("pass1"));
//            user2.setNickname("fullName");
//            user2.setUserType(UserType.Customer);
//            userRepository.save(user2);
//
//            User user3 = new User();
//            user3.setEmail("customer2@customer.org");
//            user3.setPassword(passwordEncoder.encode("pass1"));
//            user3.setUserType(UserType.Customer);
//            userRepository.save(user3);
//
//            User user4 = new User();
//            user4.setEmail("carrier@carrier.org");
//            user4.setPassword(passwordEncoder.encode("pass1"));
//            user4.setUserType(UserType.Carrier);
//            userRepository.save(user4);
//
//            User user5 = new User();
//            user5.setEmail("carrier2@carrier.org");
//            user5.setPassword(passwordEncoder.encode("pass1"));
//            user5.setUserType(UserType.Carrier);
//            userRepository.save(user5);
//
//            TransportOrder order = new TransportOrder();
//            order.setCustomer(user2);
//            order.setCompleted(false);
//            order.setFromLocation("a");
//            order.setToLocation("b");
//            order.setStartDateFrom(Date.valueOf("2022-3-28"));
//            order.setStartDateTo(Date.valueOf("2022-3-28"));
//            order.setEndDateFrom(Date.valueOf("2022-3-28"));
//            order.setEndDateTo(Date.valueOf("2022-3-28"));
//            order.setProductWeightKG(21.3f);
//            order.setProductHeightCM(100);
//            order.setProductWidthCM(100);
//            order.setProductDepthCM(100);
//            orderRepository.save(order);
//
//            TransportOffer offer = new TransportOffer();
//
//            offer.setCarrier(user4);
//            offer.setTransportOrderID(order.getTransportOrderID());
//            offer.setStartDate(Date.valueOf("2022-12-12"));
//            offer.setEndDate(Date.valueOf("2022-12-12"));
//            offer.setPrice(4.59f);
//
//            offerRepository.save(offer);
//        }
//
//    }
//}
