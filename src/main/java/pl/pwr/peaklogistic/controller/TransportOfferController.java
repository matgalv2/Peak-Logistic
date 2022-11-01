package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.OperationStatus;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;
import pl.pwr.peaklogistic.model.TransportOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.services.TransportOfferService;

import java.net.URI;

@AllArgsConstructor
@RestController
public class TransportOfferController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOfferController.class);
    private final TransportOfferService transportOfferService;


    @GetMapping(value = "/transport-offers")
    public ResponseEntity<?> getAllTransportOffers(){
        return ResponseEntity.ok(transportOfferService.getAllTransportOffers().body());
    }

    @GetMapping(value = "/transport-offers/{id}")
    public ResponseEntity<?> getTransportOfferById(@PathVariable long id){
        ServiceResponse<TransportOffer> serviceResponse = transportOfferService.getTransportOfferById(id);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(serviceResponse.body());
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/transport-offers/carriers/{id}")
    public ResponseEntity<?> getTransportOffersByCarrier(@PathVariable long id){
        return ResponseEntity.ok(transportOfferService.getTransportOffersByCarrierId(id).body());
    }

    @GetMapping(value = "/transport-offers/transport-orders/{id}")
    public ResponseEntity<?> getTransportOfferByOrder(@PathVariable long id){
        return ResponseEntity.ok(transportOfferService.getTransportOffersByTransportOrderId(id).body());
    }

    // allows modifying object after creation
    @PostMapping(value = "/transport-offers")
    public ResponseEntity<?> createTransportOffer(@RequestBody PostTransportOffer postTransportOffer){
        ServiceResponse<TransportOffer> serviceResponse = transportOfferService.createTransportOffer(postTransportOffer);

        if (serviceResponse.operationStatus() == OperationStatus.Created)
            return ResponseEntity.created(URI.create("/"+serviceResponse.body().getTransportOfferID())).body(serviceResponse.body());
        else if(serviceResponse.operationStatus() == OperationStatus.NotFound)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.badRequest().build();

    }

    @Transactional
    @DeleteMapping(value = "/transport-offers/{id}")
    public ResponseEntity<?> deleteTransportOffer(@PathVariable long id){
        ServiceResponse<TransportOffer> serviceResponse = transportOfferService.deleteTransportOffer(id);
        if(serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else if(serviceResponse.operationStatus() == OperationStatus.NotFound)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.badRequest().build();
    }
}