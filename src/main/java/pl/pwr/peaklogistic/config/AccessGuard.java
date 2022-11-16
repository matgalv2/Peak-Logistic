//package pl.pwr.peaklogistic.config;
//
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import pl.pwr.peaklogistic.common.UserType;
//import pl.pwr.peaklogistic.model.JobOffer;
//import pl.pwr.peaklogistic.model.TransportOffer;
//import pl.pwr.peaklogistic.model.TransportOrder;
//import pl.pwr.peaklogistic.model.User;
//import pl.pwr.peaklogistic.repository.JobOfferRepository;
//import pl.pwr.peaklogistic.repository.TransportOfferRepository;
//import pl.pwr.peaklogistic.repository.TransportOrderRepository;
//import pl.pwr.peaklogistic.repository.UserRepository;
//
//import java.util.Optional;
//
//
//@AllArgsConstructor
//@Component
//public class AccessGuard {
//
//    UserRepository userRepository;
//    TransportOrderRepository orderRepository;
//    TransportOfferRepository offerRepository;
//    JobOfferRepository jobRepository;
//
//    public boolean checkUserByUserId(Authentication authentication, long id) {
//        String username = authentication.getName();
//        if (userRepository.existsByUsername(username))
//            return userRepository.findByUsername(username).map(x -> id == x.getUserID()).orElse(false);
//        else
//            return false;
//    }
//
//    public boolean checkOrderByOrderId(Authentication authentication, long orderID) {
//        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        Optional<TransportOrder> order = orderRepository.findById(orderID);
//
//        if (order.isEmpty() || user.isEmpty())
//            return false;
//        else
//            return user.get().getUserID().longValue() == order.get().getCustomer().getUserID().longValue()
//                    && user.get().getUserType() == UserType.Customer;
//
//    }
//
//    public boolean checkOrdersByCustomerId(Authentication authentication, long userID) {
//        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        if (user.isEmpty())
//            return false;
//        else
//            return checkUserByUserId(authentication, userID) && user.get().getUserType() == UserType.Customer;
//    }
//
//    public boolean checkOrderByCustomerId(Authentication authentication, long userID) {
//        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        if (user.isEmpty())
//            return false;
//        else
//            return checkUserByUserId(authentication, userID) && user.get().getUserType() == UserType.Customer;
//    }
//
//    public boolean checkOfferByOfferId(Authentication authentication, long offerID) {
//        String username = authentication.getName();
//        Optional<User> user = userRepository.findByUsername(username);
//        Optional<TransportOffer> offer = offerRepository.findById(offerID);
//
//        if (offer.isEmpty() || user.isEmpty())
//            return false;
//        else
//            return user.get().getUserID().longValue() == offer.get().getCarrier().getUserID().longValue()
//                    && user.get().getUserType() == UserType.Carrier;
//    }
//
//    public boolean checkOffersByCarrierId(Authentication authentication, long userID) {
//        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        if (user.isEmpty())
//            return false;
//        else
//            return checkUserByUserId(authentication, userID) && user.get().getUserType() == UserType.Carrier;
//    }
//
//    public boolean checkOfferByCarrierId(Authentication authentication, long userID) {
//        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        if (user.isEmpty())
//            return false;
//        else
//            return checkUserByUserId(authentication, userID) && user.get().getUserType() == UserType.Carrier;
//    }
//
//    public boolean checkJobOfferByJobOfferId(Authentication authentication, long jobOfferID) {
//        String username = authentication.getName();
//        Optional<User> user = userRepository.findByUsername(username);
//        Optional<JobOffer> jobOffer = jobRepository.findById(jobOfferID);
//
//        if (jobOffer.isEmpty() || user.isEmpty())
//            return false;
//        else
//            return user.get().getUserID().longValue() == jobOffer.get().getCarrier().getUserID().longValue()
//                    && user.get().getUserType() == UserType.Carrier;
//    }
//
//    public boolean checkJobOfferByCarrierId(Authentication authentication, long userID) {
//        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        if (user.isEmpty())
//            return false;
//        else
//            return checkUserByUserId(authentication, userID) && user.get().getUserType() == UserType.Carrier;
//    }
//
//    /* Custom accessors */
//
//    public boolean carrierOrAdmin(Authentication authentication){
//        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        if (user.isEmpty())
//            return false;
//        else
//            return user.get().getUserType() == UserType.Carrier && user.get().getUserType() == UserType.Admin;
//    }
//}
