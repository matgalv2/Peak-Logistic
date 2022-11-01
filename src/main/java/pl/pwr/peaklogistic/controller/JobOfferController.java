package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.OperationStatus;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.dto.request.jobOffer.PostJobOffer;
import pl.pwr.peaklogistic.dto.response.UserResponse;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.repository.JobOfferRepository;
import pl.pwr.peaklogistic.repository.UserRepository;
import pl.pwr.peaklogistic.services.JobOfferService;

import java.net.URI;


@AllArgsConstructor
@RestController
public class JobOfferController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final JobOfferService jobOfferService;

    // only for testing
    @GetMapping(value = "/job-offers")
    public ResponseEntity<?> getAllJobOffers(){
        return ResponseEntity.ok(jobOfferService.getAllJobOffers().body());
    }

    @GetMapping(value = "/job-offers/{id}")
    public ResponseEntity<?> getJobOfferById(@PathVariable long id){
        ServiceResponse<JobOffer> serviceResponse = jobOfferService.getJobOfferById(id);

        if(serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(serviceResponse.body());
        else
            return ResponseEntity.notFound().build();

    }

    @PostMapping(value = "/job-offers")
    public ResponseEntity<?> createJobOffer(@RequestBody PostJobOffer postJobOffer) {
        ServiceResponse<JobOffer> serviceResponse = jobOfferService.createJobOffer(postJobOffer);

        if(serviceResponse.operationStatus() == OperationStatus.Created)
            return ResponseEntity.created(URI.create("/" + serviceResponse.body().getJobOfferID())).body(serviceResponse.body());
        else
            return ResponseEntity.badRequest().build();
    }


    @DeleteMapping(value = "/job-offers/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable long id){
        ServiceResponse<JobOffer> serviceResponse = jobOfferService.deleteJobOffer(id);

        if(serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();


    }
}
