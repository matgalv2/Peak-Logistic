package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.OperationStatus;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.common.Utils;
import pl.pwr.peaklogistic.common.validators.DateRangeValidator;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;
import pl.pwr.peaklogistic.dto.response.TransportOrderResponse;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.services.TransportOrderService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} )
public class TransportOrderController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOrderController.class);
    private final TransportOrderService transportOrderService;
    private final ModelMapper mapper;

    @GetMapping(value = "/transport-orders")
    public ResponseEntity<?> getAllTransportOrders() {
        return ResponseEntity.ok(transportOrderService.getAllTransportOrders().body().stream().map(toAPI()::map));
    }

    @GetMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> getTransportOrderById(@PathVariable long id) {
        ServiceResponse<TransportOrder> serviceResponse = transportOrderService.getTransportOrderById(id);
        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(toAPI().map(serviceResponse.body()));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/customers/{id}/transport-orders")
    public ResponseEntity<?> getTransportOrdersByCustomerId(@PathVariable(name = "id") long customerID) {
        return ResponseEntity.ok(transportOrderService.getTransportOrdersByCustomerId(customerID).body().stream().map(toAPI()::map));
    }

    @GetMapping(value = "/carriers/{id}/transport-orders")
    public ResponseEntity<?> getTransportOrdersContainingOfferWithCarrierId(@PathVariable(name = "id") long carrierID) {
        return ResponseEntity.ok(transportOrderService.getTransportOrdersContainingOffersWithCarrierId(carrierID).body().stream().map(toAPI()::map));
    }

    @PostMapping(value = "/customers/{id}/transport-orders")
    public ResponseEntity<?> createTransportOrder(@Valid @RequestBody PostTransportOrder postTransportOrder,
                                                  @PathVariable(name = "id") long customerID) {
        if(!DateRangeValidator.validate(postTransportOrder))
            return ResponseEntity.badRequest().body(Map.of("error", "Validation error"));

        ServiceResponse<TransportOrder> serviceResponse = transportOrderService.createTransportOrder(postTransportOrder, customerID);
        if (serviceResponse.operationStatus() == OperationStatus.Created) {
            TransportOrderResponse orderResponse = toAPI().map(serviceResponse.body());
            return ResponseEntity.created(URI.create("/" + orderResponse.getTransportOrderID())).body(orderResponse);
        } else
            return ResponseEntity.badRequest().build();
    }

    @PatchMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> setOrderAsCompleted(@PathVariable(name = "id") long orderID) {
        ServiceResponse<?> serviceResponse = transportOrderService.setTransportOrderAsCompletedById(orderID);

        if (serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else if(serviceResponse.operationStatus() == OperationStatus.BadRequest)
            return ResponseEntity.badRequest().body(Map.of("error", "Transport order is already completed"));
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> deleteTransportOrder(@PathVariable long id) {
        ServiceResponse<TransportOrder> serviceResponse = transportOrderService.deleteTransportOrder(id);
        if (serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        return Utils.handleValidationExceptions(exception);
    }

    private TypeMap<TransportOrder, TransportOrderResponse> toAPI() {
        return mapper
                .typeMap(TransportOrder.class, TransportOrderResponse.class);
    }
}