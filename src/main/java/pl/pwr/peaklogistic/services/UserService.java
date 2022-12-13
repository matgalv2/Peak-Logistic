package pl.pwr.peaklogistic.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.modelmapper.TypeMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.user.*;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.model.TransportOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.JobOfferRepository;
import pl.pwr.peaklogistic.repository.TransportOfferRepository;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransportOrderRepository orderRepository;
    private final TransportOfferRepository transportOfferRepository;
    private final JobOfferRepository jobOfferRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;


    public ServiceResponse<List<User>> getAllUsers() {
        return ServiceResponse.ok(userRepository.findAll());
    }

    public ServiceResponse<User> getUserById(long id) {
        return userRepository
                .findById(id)
                .map(ServiceResponse::ok)
                .orElse(ServiceResponse.notFound());
    }

    public ServiceResponse<List<User>> getAllCustomers() {
        return ServiceResponse.ok(
                userRepository
                        .findAll()
                        .stream()
                        .filter(user -> user.getUserType() == UserType.Customer)
                        .toList()
        );
    }

    public ServiceResponse<List<User>> getAllCarriers() {
        return ServiceResponse.ok(
                userRepository
                        .findAll()
                        .stream()
                        .filter(user -> user.getUserType() == UserType.Carrier)
                        .toList()
        );
    }

    public ServiceResponse<User> createAdmin(PostUser postUser) {
        PostUser newPostUser = PostUser.copy(postUser, encryptPassword(postUser.getPassword()));
        if(userRepository.existsByEmail(postUser.getEmail()))
            return ServiceResponse.badRequest();
        else
            return ServiceResponse.created(userRepository.save(toDomain(PostUser.class, UserType.Admin).map(newPostUser)));
    }

    public ServiceResponse<User> createCustomer(PostCustomer postCustomer) {
        PostCustomer newPostCustomer = PostCustomer.copy(postCustomer, encryptPassword(postCustomer.getPassword()));
        if(userRepository.existsByEmail(postCustomer.getEmail()))
            return ServiceResponse.badRequest();
        else
            return ServiceResponse.created(userRepository.save(toDomain(PostCustomer.class, UserType.Customer).map(newPostCustomer)));
    }

    public ServiceResponse<User> createCarrier(PostCarrier postCarrier) {
        PostCarrier newPostCarrier = PostCarrier.copy(postCarrier, encryptPassword(postCarrier.getPassword()));
        if(userRepository.existsByEmail(postCarrier.getEmail()))
            return ServiceResponse.badRequest();
        else
            return ServiceResponse.created(userRepository.save(toDomain(PostCarrier.class, UserType.Carrier).map(newPostCarrier)));
    }

    public ServiceResponse<User> updateCustomer(long id, PutCustomer putCustomer) {
        if (!userRepository.existsById(id))
            return ServiceResponse.notFound();
        else {
            User user = userRepository.findById(id).get();

            if (user.getUserType() != UserType.Customer)
                return ServiceResponse.unauthorized();

            user.updateFromCustomerRequest(putCustomer);
            return ServiceResponse.ok(userRepository.save(user));
        }
    }

    public ServiceResponse<User> updateCarrier(long id, PutCarrier putCarrier) {
        if (!userRepository.existsById(id))
            return ServiceResponse.notFound();
        else {
            User user = userRepository.findById(id).get();

            if (user.getUserType() != UserType.Carrier)
                return ServiceResponse.unauthorized();

            user.updateFromCarrierRequest(putCarrier);

            return ServiceResponse.ok(userRepository.save(user));
        }
    }

    public ServiceResponse<User> deleteUser(long id) {
        if (!userRepository.existsById(id))
            return ServiceResponse.notFound();
        else {
            //deadline is super close
            orderRepository.deleteAllById(orderRepository.getTransportOrdersByCustomerUserID(id).stream().map(TransportOrder::getTransportOrderID).toList());
            transportOfferRepository.deleteAllById(transportOfferRepository.getTransportOffersByCarrierUserID(id).stream().map(TransportOffer::getTransportOfferID).toList());
            jobOfferRepository.deleteAllById(jobOfferRepository.findAllByCarrierUserID(id).stream().map(JobOffer::getJobOfferID).toList());

            userRepository.deleteById(id);
            return ServiceResponse.noContent();
        }
    }

    public ServiceResponse<User> updateUserPassword(long id, UserPasswordRequest userPasswordRequest){
        if(!userRepository.existsById(id))
            return ServiceResponse.notFound();
        else{
            User user = userRepository.findById(id).get();
            if (!passwordEncoder.matches(userPasswordRequest.getOldPassword(), user.getPassword()) || !userPasswordRequest.getNewPassword().equals(userPasswordRequest.getRepeatedNewPassword()))
                return ServiceResponse.badRequest();
            else{
                user.setPassword(encryptPassword(userPasswordRequest.getNewPassword()));
                userRepository.save(user);
                return ServiceResponse.noContent();
            }
        }
    }


    private <K> TypeMap<K, User> toDomain(Class<K> sourceType, UserType userType) {
        return mapper.typeMap(sourceType, User.class).addMapping(src -> userType, User::setUserType);
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
