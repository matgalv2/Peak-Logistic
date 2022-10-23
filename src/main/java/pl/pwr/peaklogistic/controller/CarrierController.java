package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.dto.request.CarrierRequest;
import pl.pwr.peaklogistic.model.Carrier;
import pl.pwr.peaklogistic.repository.CarrierRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class CarrierController {
    private static final Logger logger = LoggerFactory.getLogger(CarrierController.class);
    private final CarrierRepository carrierRepository;

    @GetMapping(value = "/carriers")
    public ResponseEntity<?> getAllCarriers(){
        return ResponseEntity.ok(carrierRepository.findAll());
    }

    @GetMapping(value = "/carriers/{id}")
    public ResponseEntity<?> getCarrierById(@PathVariable long id){
        return carrierRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // allows modifying object after creation
    @PostMapping(value = "/carriers")
    public ResponseEntity<?> createCarrier(@RequestBody Carrier carrier){
        Carrier addedCarrier = carrierRepository.save(carrier);
        return ResponseEntity.created(URI.create("/" + addedCarrier.getUserID())).body(addedCarrier);
    }


    @Transactional
    @PutMapping(value = "/carriers/{id}")
    public ResponseEntity<?> updateCarrier(@PathVariable long id, @RequestBody CarrierRequest carrierRequest){
        if(!carrierRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            Carrier carrier =  carrierRepository.getReferenceById(id);
            carrier.setFullName(carrierRequest.getFullName());
            carrier.setPhoneNumber(carrierRequest.getPhoneNumber());
            carrier.setContactEmail(carrierRequest.getContactEmail());

            return ResponseEntity.ok().body(carrierRepository.save(carrier));
        }
    }

}