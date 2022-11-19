package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.OperationStatus;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.dto.request.jobOffer.PostJobOffer;
import pl.pwr.peaklogistic.dto.request.jobOffer.PutJobOffer;
import pl.pwr.peaklogistic.dto.response.JobOfferResponse;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.services.JobOfferService;

import java.net.URI;


@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} )
@AllArgsConstructor
@RestController
public class JobOfferController {

    private static final Logger logger = LoggerFactory.getLogger(JobOffer.class);
    private final JobOfferService jobOfferService;
    private final ModelMapper mapper;


    // only for testing
    @GetMapping(value = "/job-offers")
    public ResponseEntity<?> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers().body().stream().map(toAPI()::map));
    }

    @GetMapping(value = "/job-offers/{id}")
    public ResponseEntity<?> getJobOfferById(@PathVariable long id) {
        ServiceResponse<JobOffer> serviceResponse = jobOfferService.getJobOfferById(id);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(toAPI().map(serviceResponse.body()));
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "carriers/{id}/job-offers")
    public ResponseEntity<?> createJobOffer(@RequestBody PostJobOffer postJobOffer, @PathVariable(name = "id") long carrierID) {
        ServiceResponse<JobOffer> serviceResponse = jobOfferService.createJobOffer(postJobOffer, carrierID);

        if (serviceResponse.operationStatus() == OperationStatus.Created) {
            JobOfferResponse jobResponse = toAPI().map(serviceResponse.body());
            return ResponseEntity.created(URI.create("/" + jobResponse.getJobOfferID())).body(jobResponse);
        } else
            return ResponseEntity.badRequest().build();
    }


    @PutMapping(value = "/job-offers/{id}")
    public ResponseEntity<?> updateJobOffer(@RequestBody PutJobOffer putJobOffer, @PathVariable(name = "id") long jobOfferID) {
        ServiceResponse<JobOffer> serviceResponse = jobOfferService.updateJobOffer(putJobOffer, jobOfferID);

        if (serviceResponse.operationStatus() == OperationStatus.Ok)
            return ResponseEntity.ok(toAPI().map(serviceResponse.body()));
        else
            return ResponseEntity.badRequest().build();
    }


    @DeleteMapping(value = "/job-offers/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable long id) {
        ServiceResponse<JobOffer> serviceResponse = jobOfferService.deleteJobOffer(id);

        if (serviceResponse.operationStatus() == OperationStatus.NoContent)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    private TypeMap<JobOffer, JobOfferResponse> toAPI() {
        return mapper.typeMap(JobOffer.class, JobOfferResponse.class);
    }
}
