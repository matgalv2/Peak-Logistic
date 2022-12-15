package pl.pwr.peaklogistic.config;


import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.model.TransportOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.JobOfferRepository;
import pl.pwr.peaklogistic.repository.TransportOfferRepository;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Component
public class AccessGuard {

    final private UserRepository userRepository;
    final private TransportOrderRepository orderRepository;
    final private TransportOfferRepository offerRepository;
    final private JobOfferRepository jobRepository;


    private boolean isCustomer(UserType type){ return type.equals(UserType.Customer);}
    private boolean isCarrier(UserType type){ return type.equals(UserType.Carrier);}
    private boolean isAdmin(UserType type){ return type.equals(UserType.Admin);}


    public boolean checkUserByUserId(Authentication authentication, long id) {
        return userRepository.findByEmail(authentication.getName()).map(x -> id == x.getUserID() || isAdmin(x.getUserType())).orElse(false);
    }


    public boolean checkOrderByOrderId(Authentication authentication, long orderID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        Optional<TransportOrder> order = orderRepository.findById(orderID);

        if (order.isEmpty() || user.isEmpty())
            return false;
        else
            return user.get().getUserID().longValue() == order.get().getCustomer().getUserID().longValue()
                    && isCustomer(user.get().getUserType()) || isAdmin(user.get().getUserType());

    }


    public boolean checkOrderByCustomerId(Authentication authentication, long userID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            return false;
        else
            return checkUserByUserId(authentication, userID) && isCustomer(user.get().getUserType()) || isAdmin(user.get().getUserType());
    }

    public boolean checkOrderByRoleOrOrderId(Authentication authentication, long orderID){
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            return false;
        else
            return checkOrderByOrderId(authentication, orderID) && isCustomer(user.get().getUserType()) || isCarrier(user.get().getUserType()) || isAdmin(user.get().getUserType());

    }

    public boolean checkOfferByOfferId(Authentication authentication, long offerID) {
        String username = authentication.getName();
        Optional<User> user = userRepository.findByEmail(username);
        Optional<TransportOffer> offer = offerRepository.findById(offerID);

        if (offer.isEmpty() || user.isEmpty())
            return false;
        else
            return user.get().getUserID().longValue() == offer.get().getCarrier().getUserID().longValue()
                    && isCarrier(user.get().getUserType()) || isAdmin(user.get().getUserType());
    }

    public boolean checkOffersByCarrierId(Authentication authentication, long userID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            return false;
        else
            return checkUserByUserId(authentication, userID) && isCarrier(user.get().getUserType()) || isAdmin(user.get().getUserType());
    }

    public boolean checkOfferByCarrierId(Authentication authentication, long userID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            return false;
        else
            return checkUserByUserId(authentication, userID) && isCarrier(user.get().getUserType()) || isAdmin(user.get().getUserType());
    }

    public boolean checkJobOfferByJobOfferId(Authentication authentication, long jobOfferID) {
        String username = authentication.getName();
        Optional<User> user = userRepository.findByEmail(username);
        Optional<JobOffer> jobOffer = jobRepository.findById(jobOfferID);

        if (jobOffer.isEmpty() || user.isEmpty())
            return false;
        else
            return user.get().getUserID().longValue() == jobOffer.get().getCarrier().getUserID().longValue()
                    && isCarrier(user.get().getUserType()) || isAdmin(user.get().getUserType());
    }

    public boolean checkJobOfferByCarrierId(Authentication authentication, long userID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            return false;
        else
            return checkUserByUserId(authentication, userID) && isCarrier(user.get().getUserType()) || isAdmin(user.get().getUserType());
    }

    /* Custom accessors */

    public boolean carrierOrAdmin(Authentication authentication) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            return false;
        else
            return isCarrier(user.get().getUserType()) || isAdmin(user.get().getUserType());
    }

    public boolean loggedUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).isPresent();
    }
}
