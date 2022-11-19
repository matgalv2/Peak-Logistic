package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.OperationStatus;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;
import pl.pwr.peaklogistic.dto.response.CarrierResponse;
import pl.pwr.peaklogistic.dto.response.CustomerResponse;
import pl.pwr.peaklogistic.dto.response.TransportOfferResponse;
import pl.pwr.peaklogistic.dto.response.TransportOrderResponse;
import pl.pwr.peaklogistic.model.TransportOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.services.TransportOfferService;

import java.net.URI;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} )
@AllArgsConstructor
@RestController
public class TransportOfferController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOfferController.class);
    private final TransportOfferService transportOfferService;
    private final ModelMapper mapper;


    @GetMapping(value = "/transport-offers")
    public ResponseEntity<?> getAllTransportOffers() {
        return ResponseEntity.ok(transportOfferService.getAllTransportOffers().body().stream().map(toAPI()::map));
    }

    @GetMapping(value = "/transport-offers/{id}")
    public ResponseEntity<?> getTransportOfferById(@PathVariable long id) {
        ServiceResponse<TransportOffer> serviceResponse = transportOfferService.getTransportOfferById(id);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(toAPI().map(serviceResponse.body()));
        else
            return ResponseEntity.notFound().build();
    }

//    @GetMapping(value = "/transport-orders/{id}/transport-offers")
//    public ResponseEntity<?> getTransportOfferByOrder(@PathVariable long id){
//        return ResponseEntity.ok(transportOfferService.getTransportOffersByTransportOrderId(id).body());
//    }

    @GetMapping(value = "/carriers/{id}/transport-offers")
    public ResponseEntity<?> getTransportOffersByCarrier(@PathVariable long id) {
        return ResponseEntity.ok(transportOfferService.getTransportOffersByCarrierId(id).body().stream().map(toAPI()::map));
    }

    // allows modifying object after creation
    @PostMapping(value = "/carriers/{id}/transport-offers")
    public ResponseEntity<?> createTransportOffer(@RequestBody PostTransportOffer postTransportOffer,
                                                  @PathVariable(name = "id") long carrierID) {
        ServiceResponse<TransportOffer> serviceResponse = transportOfferService.createTransportOffer(postTransportOffer, carrierID);

        if (serviceResponse.operationStatus() == OperationStatus.Created) {
            TransportOfferResponse offerResponse = toAPI().map(serviceResponse.body());
            return ResponseEntity.created(URI.create("/" + offerResponse.getTransportOfferID())).body(offerResponse);
        } else if (serviceResponse.operationStatus() == OperationStatus.NotFound)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.badRequest().build();

    }

    @Transactional
    @DeleteMapping(value = "/transport-offers/{id}")
    public ResponseEntity<?> deleteTransportOffer(@PathVariable long id) {
        ServiceResponse<TransportOffer> serviceResponse = transportOfferService.deleteTransportOffer(id);
        if (serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else if (serviceResponse.operationStatus() == OperationStatus.NotFound)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.badRequest().build();
    }


    private TypeMap<TransportOffer, TransportOfferResponse> toAPI() {
        return mapper.typeMap(TransportOffer.class, TransportOfferResponse.class);
    }
}