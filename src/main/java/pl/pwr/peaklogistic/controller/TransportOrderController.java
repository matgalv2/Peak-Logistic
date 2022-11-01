package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.OperationStatus;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.services.TransportOrderService;

import java.net.URI;

@AllArgsConstructor
@RestController
public class TransportOrderController {
    private static final Logger logger = LoggerFactory.getLogger(TransportOrderController.class);
    private final TransportOrderService transportOrderService;

    @GetMapping(value = "/transport-orders")
    public ResponseEntity<?> getAllTransportOrders(){
        return ResponseEntity.ok(transportOrderService.getAllTransportOrders().body());
    }

    @GetMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> getTransportOrderById(@PathVariable long id){
        ServiceResponse<TransportOrder> serviceResponse = transportOrderService.getTransportOrderById(id);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(serviceResponse.body());
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/transport-orders/customer/{id}")
    public ResponseEntity<?> getTransportOrdersByCustomerId(@PathVariable long id){
        return ResponseEntity.ok(transportOrderService.getTransportOrdersByCustomerId(id).body());
    }

    @PostMapping(value = "/transport-orders")
    public ResponseEntity<?> createTransportOrder(@RequestBody PostTransportOrder postTransportOrder){

        ServiceResponse<TransportOrder> serviceResponse = transportOrderService.createTransportOrder(postTransportOrder);

        if (serviceResponse.operationStatus() == OperationStatus.Created)
            return ResponseEntity.created(URI.create("/"+serviceResponse.body().getTransportOrderID())).body(serviceResponse.body());
        else
            return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/transport-orders/{id}")
    public ResponseEntity<?> deleteTransportOrder(@PathVariable long id){
        ServiceResponse<TransportOrder> serviceResponse = transportOrderService.deleteTransportOrder(id);
        if(serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();

    }

}