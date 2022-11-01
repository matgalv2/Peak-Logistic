package pl.pwr.peaklogistic.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.dto.request.jobOffer.PostJobOffer;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.JobOfferRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    public ServiceResponse<List<JobOffer>> getAllJobOffers()
    {
         return ServiceResponse.ok(jobOfferRepository.findAll());
    }

    public ServiceResponse<JobOffer> getJobOfferById(long id){
        return jobOfferRepository.findById(id).map(ServiceResponse::ok).orElse(ServiceResponse.notFound());
    }


    public ServiceResponse<JobOffer> createJobOffer(PostJobOffer postJobOffer){
        return userRepository
                .findById(postJobOffer.getCarrierID())
                .map(x -> ServiceResponse.ok(jobOfferRepository.save(mapperWithUser(x).map(postJobOffer))))
                .orElse(ServiceResponse.badRequest());

    }

    public ServiceResponse<JobOffer> deleteJobOffer(long id){
        if (!jobOfferRepository.existsById(id))
            return ServiceResponse.notFound();
        else{
            jobOfferRepository.deleteById(id);
            return ServiceResponse.noContent();
        }
    }

    private TypeMap<PostJobOffer, JobOffer> mapperWithUser(User user){
        return mapper
//                .addMappings(mapper -> mapper.map(src -> user, JobOffer::setCarrier));
                .typeMap(PostJobOffer.class, JobOffer.class)
                .addMapping(src -> user, JobOffer::setCarrier);
    }


}
