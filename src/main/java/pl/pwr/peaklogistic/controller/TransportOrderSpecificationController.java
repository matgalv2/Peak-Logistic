package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.model.TransportOrderSpecification;
import pl.pwr.peaklogistic.repository.TransportOrderSpecificationRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class TransportOrderSpecificationController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOrderSpecificationController.class);
    private final TransportOrderSpecificationRepository transportOrderSpecificationRepository;

    @GetMapping(value = "/transport-order-specifications")
    public ResponseEntity<?> getAllOffersSpecification(){
        return ResponseEntity.ok(transportOrderSpecificationRepository.findAll());
    }

    @GetMapping(value = "/transport-order-specifications/{id}")
    public ResponseEntity<?> getOfferSpecificationById(@PathVariable long id){
        return transportOrderSpecificationRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    // allows modifying object after creation
    @PostMapping(value = "/transport-order-specifications")
    public ResponseEntity<?> createOfferSpecification(@RequestBody TransportOrderSpecification offerSpecification){
        TransportOrderSpecification addedOfferSpecification = transportOrderSpecificationRepository.save(offerSpecification);
        return ResponseEntity.created(URI.create("/" + addedOfferSpecification.getTransportOrderSpecificationID())).body(addedOfferSpecification);
    }



//    @Transactional
//    @PutMapping(value = "/offerSpecifications/{id}")
//    public ResponseEntity<?> updateOffer(@PathVariable long id, @RequestBody OfferSpecification offerSpecification){
//        if(!offerSpecificationRepository.existsById(id))
//            return ResponseEntity.notFound().build();
//        else{
//            OfferSpecification foundOffer = offerSpecificationRepository.getReferenceById(id);
////            offer.setFullName(customerRequest.getFullName());
//            return ResponseEntity.ok().body(offerSpecificationRepository.save(offerSpecification));
//        }
//    }

    @Transactional
    @DeleteMapping(value = "/transport-order-specifications/{id}")
    public ResponseEntity<?> deleteTransportOrderSpecification(@PathVariable long id){
        if(!transportOrderSpecificationRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            transportOrderSpecificationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}