package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.CarrierRequest;
import pl.pwr.peaklogistic.dto.request.CustomerRequest;
import pl.pwr.peaklogistic.dto.request.UserRequest;
import pl.pwr.peaklogistic.dto.response.CarrierResponse;
import pl.pwr.peaklogistic.dto.response.CustomerResponse;
import pl.pwr.peaklogistic.dto.response.UserResponse;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

//    @Bean
//    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/users")
    public ResponseEntity<?> getAllUsers(){
//        return ResponseEntity.ok(userRepository.findAll().stream().map(UserResponse::toAPI));
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id){
        return userRepository.findById(id).map(user -> ResponseEntity.ok(UserResponse.toAPI(user))).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/customers")
    public ResponseEntity<?> getAllCustomers(){
        return ResponseEntity.ok(userRepository.findAll().stream().filter(user -> user.getUserType() == UserType.Customer).map(CustomerResponse::fromUser));
    }

    @GetMapping(value = "/carriers")
    public ResponseEntity<?> getAllCarriers(){
        return ResponseEntity.ok(userRepository.findAll().stream().filter(user -> user.getUserType() == UserType.Carrier).map(CarrierResponse::fromUser));
    }



    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest){
//        UserRequest hashedPasswd = new UserRequest(userRequest.getEmail(), passwordEncoder.encode(userRequest.getPassword()), userRequest.getUserType());
//        User addedUser = userRepository.save(UserRequest.toDomain(hashedPasswd));
        User addedUser = userRepository.save(UserRequest.toDomain(userRequest));
        return ResponseEntity.created(URI.create("/" + addedUser.getUserID())).body(UserResponse.toAPI(addedUser));
    }


//    @Transactional
//    @PutMapping(value = "/users/{id}/passwd")
//    public ResponseEntity<?> updatePassword(@PathVariable long id, @RequestBody UserPasswordRequest userPasswordRequest){
//        if(!userRepository.existsById(id))
//            return ResponseEntity.notFound().build();
//        else{
//            User user = userRepository.getReferenceById(id);
//            user.setPassword(userPasswordRequest.getNewPassword());
//            return ResponseEntity.ok().body(userRepository.save(user));
//        }
//    }

    @Transactional
    @PutMapping(value = "/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable long id, @RequestBody CustomerRequest customerRequest){
        if(!userRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            User user = userRepository.findById(id).get();

            if(user.getUserType() != UserType.Customer)
                return ResponseEntity.badRequest().build();

            user.updateFromCustomerRequest(customerRequest);
            return ResponseEntity.ok().body(CustomerResponse.fromUser(userRepository.save(user)));
        }
    }

    @Transactional
    @PutMapping(value = "/carriers/{id}")
    public ResponseEntity<?> updateCarrier(@PathVariable long id, @RequestBody CarrierRequest carrierRequest){
        if(!userRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            User user = userRepository.findById(id).get();

            if(user.getUserType() != UserType.Carrier)
                return ResponseEntity.badRequest().build();

            user.updateFromCarrierRequest(carrierRequest);

            return ResponseEntity.ok().body(CustomerResponse.fromUser(userRepository.save(user)));
        }
    }





    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        if(!userRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}