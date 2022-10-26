package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.JobOffer.PostJobOffer;
import pl.pwr.peaklogistic.dto.request.JobOffer.PutJobOffer;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.JobOfferRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.net.URI;


@AllArgsConstructor
@RestController
public class JobOfferController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final JobOfferRepository jobOfferRepository;
    private final UserRepository userRepository;

    // only for testing
    @GetMapping(value = "/job-offers")
    public ResponseEntity<?> getAllJobOffers(){
        return ResponseEntity.ok(jobOfferRepository.findAll());
    }

    @GetMapping(value = "/job-offers/{id}")
    public ResponseEntity<?> getJobOfferById(@PathVariable long id){
        return jobOfferRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/job-offers")
    public ResponseEntity<?> createJobOffer(@RequestBody PostJobOffer postJobOffer){

        if(!userRepository.existsById(postJobOffer.getCarrierID()))
            return ResponseEntity.badRequest().build();

        else{

            if(userRepository.findById(postJobOffer.getCarrierID()).get().getUserType() != UserType.Carrier)
                return ResponseEntity.badRequest().build();

            User carrier = userRepository.findById(postJobOffer.getCarrierID()).get();
            JobOffer addedJobOffer = jobOfferRepository.save(JobOffer.fromPostRequest(postJobOffer, carrier));

            return ResponseEntity.created(URI.create("/" + addedJobOffer.getJobOfferID())).body(addedJobOffer);
        }
    }

//    @PutMapping(value = "/job-offers/{id}")
//    public ResponseEntity<?> updateJobOffer(@PathVariable long id, @RequestBody PutJobOffer putJobOffer){
//
//    }

    @DeleteMapping(value = "/job-offers/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable long id){
        if(!jobOfferRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            jobOfferRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
