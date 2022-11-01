package pl.pwr.peaklogistic.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.modelmapper.TypeMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.user.*;
import pl.pwr.peaklogistic.dto.response.CarrierResponse;
import pl.pwr.peaklogistic.dto.response.CustomerResponse;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;


    public ServiceResponse<List<User>> getAllUsers(){
        return ServiceResponse.ok(userRepository.findAll());
    }

    public ServiceResponse<User> getUserById(long id){
        return userRepository
                .findById(id)
                .map(ServiceResponse::ok)
                .orElse(ServiceResponse.notFound());
    }

    public ServiceResponse<List<User>> getAllCustomers(){
        return ServiceResponse.ok(
                userRepository
                .findAll()
                .stream()
                .filter(user -> user.getUserType() == UserType.Customer)
                .toList()
        );
    }

    public ServiceResponse<List<User>> getAllCarriers(){
        return ServiceResponse.ok(
                userRepository
                        .findAll()
                        .stream()
                        .filter(user -> user.getUserType() == UserType.Carrier)
                        .toList()
        );
    }

    public ServiceResponse<User> createUser(PostUser postUser){
        PostUser newPostUser = PostUser.copy(postUser, encryptPassword(postUser.getPassword()));
        return ServiceResponse.created(userRepository.save(toDomainMapper(PostUser.class).map(newPostUser)));
    }

    public ServiceResponse<User> createCustomer(PostCustomer postCustomer){
        PostCustomer newPostCustomer = PostCustomer.copy(postCustomer, encryptPassword(postCustomer.getPassword()));
        return ServiceResponse.created(userRepository.save(toDomainMapper(PostCustomer.class).map(newPostCustomer)));
    }

    public ServiceResponse<User> createCarrier(PostCarrier postCarrier){
        PostCarrier newPostCarrier = PostCarrier.copy(postCarrier, encryptPassword(postCarrier.getPassword()));
        return ServiceResponse.created(userRepository.save(toDomainMapper(PostCarrier.class).map(newPostCarrier)));
    }

    public ServiceResponse<User> updateCustomer(long id, PutCustomer putCustomer){
        if(!userRepository.existsById(id))
            return ServiceResponse.notFound();
        else{
            User user = userRepository.findById(id).get();

            if(user.getUserType() != UserType.Customer)
                return ServiceResponse.unauthorized();

            user.updateFromCustomerRequest(putCustomer);
            return ServiceResponse.ok(userRepository.save(user));
        }
    }

    public ServiceResponse<User> updateCarrier(long id, PutCarrier putCarrier){
        if(!userRepository.existsById(id))
            return ServiceResponse.notFound();
        else{
            User user = userRepository.findById(id).get();

            if(user.getUserType() != UserType.Carrier)
                return ServiceResponse.unauthorized();

            user.updateFromCarrierRequest(putCarrier);

            return ServiceResponse.ok(userRepository.save(user));
        }
    }

    public ServiceResponse<User> deleteUser(long id){
        if(!userRepository.existsById(id))
            return ServiceResponse.notFound();
        else{
            userRepository.deleteById(id);
            return ServiceResponse.noContent();
        }
    }





    private <K> TypeMap<K, User> toDomainMapper(Class<K> sourceType){
        return mapper.typeMap(sourceType, User.class);
    }

    private String encryptPassword(String password){
        return passwordEncoder.encode(password);
    }

}
