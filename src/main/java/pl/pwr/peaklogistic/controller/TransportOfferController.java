package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.TransportOfferRequest;
import pl.pwr.peaklogistic.model.TransportOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.TransportOfferRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class TransportOfferController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOfferController.class);
    private final TransportOrderRepository transportOrderRepository;
    private final TransportOfferRepository transportOfferRepository;
    private final UserRepository userRepository;

    @GetMapping(value = "/transport-order-specifications")
    public ResponseEntity<?> getAllTransportOffers(){
        return ResponseEntity.ok(transportOfferRepository.findAll());
    }

    @GetMapping(value = "/transport-order-specifications/{id}")
    public ResponseEntity<?> getTransportOfferById(@PathVariable long id){
        return transportOfferRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // allows modifying object after creation
    @PostMapping(value = "/transport-order-specifications")
    public ResponseEntity<?> createTransportOffer(@RequestBody TransportOfferRequest transportOfferRequest){
        if (!transportOrderRepository.existsById(transportOfferRequest.getTransportOrderID()) ||
                !userRepository.existsById(transportOfferRequest.getCarrierID())
        )
            return ResponseEntity.badRequest().build();
        else {

            if(userRepository.findById(transportOfferRequest.getCarrierID()).get().getUserType() != UserType.Carrier)
                return ResponseEntity.badRequest().build();

            User carrier = userRepository.findById(transportOfferRequest.getCarrierID()).get();
            TransportOrder transportOrder = transportOrderRepository.findById(transportOfferRequest.getTransportOrderID()).get();

            TransportOffer addedTransportOffer = transportOfferRepository.save(TransportOffer.fromRequest(transportOfferRequest, transportOrder, carrier));
            return ResponseEntity.created(URI.create("/" + addedTransportOffer.getTransportOfferID())).body(addedTransportOffer);
        }


    }

    @Transactional
    @DeleteMapping(value = "/transport-order-specifications/{id}")
    public ResponseEntity<?> deleteTransportOffer(@PathVariable long id){
        if(!transportOfferRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            transportOfferRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}