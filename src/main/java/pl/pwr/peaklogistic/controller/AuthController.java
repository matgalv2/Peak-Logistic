package pl.pwr.peaklogistic.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.Utils;
import pl.pwr.peaklogistic.dto.request.user.PostCarrier;
import pl.pwr.peaklogistic.dto.request.user.PostCustomer;
import pl.pwr.peaklogistic.dto.request.user.PostUser;
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
public class AuthController {
    private final UserService userService;
    private final ModelMapper mapper;

    // TODO: refresh token
//    @GetMapping("/refresh-token")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
//    }



    @PostMapping(value = "/admins")
    public ResponseEntity<?> createUser(@Valid @RequestBody PostUser postUser) {
        User addedUser = userService.createAdmin(postUser).body();
        UserResponse user = toAPI(UserResponse.class).map(addedUser);
        return ResponseEntity.created(URI.create("/" + user.getUserID())).body(user);
    }

    @PostMapping(value = "/carriers")
    public ResponseEntity<?> createCarrier(@Valid @RequestBody PostCarrier postCarrier) {
        User addedCarrier = userService.createCarrier(postCarrier).body();
        CarrierResponse carrier = toAPI(CarrierResponse.class).map(addedCarrier);
        return ResponseEntity.created(URI.create("/" + carrier.getUserID())).body(carrier);
    }

    @PostMapping(value = "/customers")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody PostCustomer postCustomer) {
        User addedCustomer = userService.createCustomer(postCustomer).body();
        CustomerResponse customer = toAPI(CustomerResponse.class).map(addedCustomer);
        return ResponseEntity.created(URI.create("/" + customer.getUserID())).body(customer);
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
