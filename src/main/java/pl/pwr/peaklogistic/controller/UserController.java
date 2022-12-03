package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.OperationStatus;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.common.Utils;
import pl.pwr.peaklogistic.dto.request.user.*;
import pl.pwr.peaklogistic.dto.response.CarrierResponse;
import pl.pwr.peaklogistic.dto.response.CustomerResponse;
import pl.pwr.peaklogistic.dto.response.UserResponse;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.services.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} )
@AllArgsConstructor
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping(value = "/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().body().stream().map(toAPI(UserResponse.class)::map));
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        ServiceResponse<User> serviceResponse = userService.getUserById(id);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(toAPI(UserResponse.class).map(serviceResponse.body()));
        else
            return ResponseEntity.notFound().build();


    }

    @GetMapping(value = "/customers")
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers().body().stream().map(toAPI(CustomerResponse.class)::map));
    }

    @GetMapping(value = "/carriers")
    public ResponseEntity<?> getAllCarriers() {
        return ResponseEntity.ok(userService.getAllCarriers().body().stream().map(user -> toAPI(CarrierResponse.class).map(user)));
    }


//    @PostMapping(value = "/admins")
//    public ResponseEntity<?> createUser(@RequestBody PostUser postUser){
//        User addedUser = userService.createAdmin(postUser).body();
//        UserResponse user = toAPI(UserResponse.class).map(addedUser);
//        return ResponseEntity.created(URI.create("/" + user.getUserID())).body(user);
//    }
//
//    @PostMapping(value = "/carriers")
//    public ResponseEntity<?> createCarrier(@RequestBody PostCarrier postCarrier){
//        User addedCarrier = userService.createCarrier(postCarrier).body();
//        CarrierResponse carrier = toAPI(CarrierResponse.class).map(addedCarrier);
//        return ResponseEntity.created(URI.create("/" + carrier.getUserID())).body(carrier);
//    }
//
//    @PostMapping(value = "/customers")
//    public ResponseEntity<?> createCustomer(@RequestBody PostCustomer postCustomer){
//        User addedCustomer = userService.createCustomer(postCustomer).body();
//        CustomerResponse customer = toAPI(CustomerResponse.class).map(addedCustomer);
//        return ResponseEntity.created(URI.create("/" + customer.getUserID())).body(customer);
//    }


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
    public ResponseEntity<?> updateCustomer(@PathVariable long id, @Valid @RequestBody PutCustomer putCustomer) {
        ServiceResponse<User> serviceResponse = userService.updateCustomer(id, putCustomer);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(toAPI(CustomerResponse.class).map(serviceResponse.body()));
        else
            return ResponseEntity.notFound().build();
    }

    @Transactional
    @PutMapping(value = "/carriers/{id}")
    public ResponseEntity<?> updateCarrier(@PathVariable long id, @Valid @RequestBody PutCarrier putCarrier) {
        ServiceResponse<User> serviceResponse = userService.updateCarrier(id, putCarrier);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(toAPI(CustomerResponse.class).map(serviceResponse.body()));
        else
            return ResponseEntity.notFound().build();
    }


    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        ServiceResponse<User> serviceResponse = userService.deleteUser(id);

        if (serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        return Utils.handleValidationExceptions(exception);
    }

    private <K> TypeMap<User, K> toAPI(Class<K> destinationType) {
        return mapper.typeMap(User.class, destinationType);
    }
}