package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.dto.request.TransportOrderRequest;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class TransportOrderController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOrderController.class);
    private final TransportOrderRepository transportOrderRepository;

    @GetMapping(value = "/transport-orders")
    public ResponseEntity<?> getAllOffers(){
        return ResponseEntity.ok(transportOrderRepository.findAll());
    }

    @GetMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> getOfferById(@PathVariable long id){
        return transportOrderRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    // allows modifying object after creation
    @PostMapping(value = "/transport-orders")
    public ResponseEntity<?> createOffer(@RequestBody TransportOrderRequest transportOrderRequest){

        TransportOrder addedTransportOrder = transportOrderRepository.save(TransportOrder.toDomain(transportOrderRequest));
        return ResponseEntity.created(URI.create("/" + addedTransportOrder.getTransportOrderID())).body(addedTransportOrder);
    }

    @DeleteMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable long id){
        if(!transportOrderRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            transportOrderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}