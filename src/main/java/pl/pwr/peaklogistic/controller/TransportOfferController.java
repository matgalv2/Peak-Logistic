package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;
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
    public ResponseEntity<?> createTransportOffer(@RequestBody PostTransportOffer postTransportOffer){
        if (!transportOrderRepository.existsById(postTransportOffer.getTransportOrderID()) ||
                !userRepository.existsById(postTransportOffer.getCarrierID())
        )
            return ResponseEntity.badRequest().build();
        else {

            if(userRepository.findById(postTransportOffer.getCarrierID()).get().getUserType() != UserType.Carrier)
                return ResponseEntity.badRequest().build();

            User carrier = userRepository.findById(postTransportOffer.getCarrierID()).get();
            TransportOrder transportOrder = transportOrderRepository.findById(postTransportOffer.getTransportOrderID()).get();

            TransportOffer addedTransportOffer = transportOfferRepository.save(TransportOffer.fromRequest(postTransportOffer, transportOrder, carrier));
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