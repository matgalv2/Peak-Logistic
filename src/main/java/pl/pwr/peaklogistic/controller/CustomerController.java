package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.dto.request.CustomerRequest;
import pl.pwr.peaklogistic.model.Customer;
import pl.pwr.peaklogistic.repository.CustomerRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;

    @GetMapping(value = "/customers")
    public ResponseEntity<?> getAllCustomers(){
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @GetMapping(value = "/customers/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable long id){
        return customerRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // allows modifying object after creation
    @PostMapping(value = "/customers")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer){
        Customer addedCustomer = customerRepository.save(customer);
        return ResponseEntity.created(URI.create("/" + addedCustomer.getUserID())).body(addedCustomer);
    }


    @Transactional
    @PutMapping(value = "/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable long id, @RequestBody CustomerRequest customerRequest){
        if(!customerRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            Customer customer = customerRepository.getReferenceById(id);
            customer.setFullName(customerRequest.getFullName());
            return ResponseEntity.ok().body(customerRepository.save(customer));
        }
    }

//    @DeleteMapping(value = "/users/{id}")
//    public ResponseEntity<?> deleteUser(@RequestParam long id){
//        if(!userRepository.existsById(id))
//            return ResponseEntity.notFound().build();
//        else{
//            userRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//    }
}