package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class TransportOrderController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOrderController.class);
    private final TransportOrderRepository transportOrderRepository;
    private final UserRepository userRepository;

    @GetMapping(value = "/transport-orders")
    public ResponseEntity<?> getAllTransportOrders(){
        return ResponseEntity.ok(transportOrderRepository.findAll());
    }

    @GetMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> getTransportOrderById(@PathVariable long id){
        return transportOrderRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/transport-orders")
    public ResponseEntity<?> createTransportOrder(@RequestBody PostTransportOrder postTransportOrder){

        if(!userRepository.existsById(postTransportOrder.getCustomerID()))
            return ResponseEntity.badRequest().build();
        else{
            if(userRepository.findById(postTransportOrder.getCustomerID()).get().getUserType() != UserType.Customer)
                return ResponseEntity.badRequest().build();

            User customer = userRepository.findById(postTransportOrder.getCustomerID()).get();
            TransportOrder addedTransportOrder = transportOrderRepository.save(TransportOrder.fromRequest(postTransportOrder, customer));

            return ResponseEntity.created(URI.create("/" + addedTransportOrder.getTransportOrderID())).body(addedTransportOrder);
        }
    }

    @DeleteMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> deleteTransportOrder(@PathVariable long id){
        if(!transportOrderRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            transportOrderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}