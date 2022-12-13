package pl.pwr.peaklogistic.integration.jobOffer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.peaklogistic.PeakLogisticApplication;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.JobOfferRepository;
import pl.pwr.peaklogistic.repository.UserRepository;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = PeakLogisticApplication.class)
public class JobOfferRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JobOfferRepository jobOfferRepository;

    private User createCarrier()
    {
        User user = new User();
        user.setUserType(UserType.Carrier);
        user.setPassword("password");
        user.setEmail("peaklogistics@me.eu");
        user.setCompanyName("transportex");
        user.setTaxIdentificationNumber("1234567890");
        user.setPhone("+1 345678123");

        return user;
    }

    private JobOffer generateJobOffer(User carrier){

        JobOffer jobOffer = new JobOffer();

        jobOffer.setCarrier(carrier);
        jobOffer.setContactEmail("trebor7@o2.pl");
        jobOffer.setContentENG("Some content");
        jobOffer.setContentPL("Jakaś treść");
        jobOffer.setTitleENG("Some title");
        jobOffer.setTitlePL("Jakiś tytuł");

        return jobOffer;
    }



    @Test
    @Rollback
    @Transactional
    public void getJobOfferById(){
        User carrier = userRepository.save(createCarrier());
        User addedCarrier = userRepository.save(carrier);

        JobOffer jobOffer = generateJobOffer(addedCarrier);

        jobOffer.setTitlePL("tytuł 1");
        JobOffer addedJobOffer = jobOfferRepository.save(jobOffer);

        Optional<JobOffer> result = jobOfferRepository.findById(addedJobOffer.getJobOfferID());

        assertEquals(result, Optional.of(addedJobOffer));


    }

    @Test
    @Rollback
    @Transactional
    public void getAllJobOffers(){
        User carrier = userRepository.save(createCarrier());
        User addedCarrier = userRepository.save(carrier);

        JobOffer jobOffer = generateJobOffer(addedCarrier);
        JobOffer expectedJobOffer = jobOfferRepository.save(jobOffer);

        JobOffer jobOffer2 = generateJobOffer(addedCarrier);
        JobOffer expectedJobOffer2 = jobOfferRepository.save(jobOffer2);

        List<JobOffer> result = jobOfferRepository.findAll();

        assertEquals(List.of(expectedJobOffer, expectedJobOffer2), result);
    }

    @Test
    @Rollback
    @Transactional
    public void createJobOffer(){
        User carrier = userRepository.save(createCarrier());
        User addedCarrier = userRepository.save(carrier);

        JobOffer jobOffer = generateJobOffer(addedCarrier);
        JobOffer expectedJobOffer = jobOfferRepository.save(jobOffer);

        Optional<JobOffer> foundJobOffer = jobOfferRepository.findById(expectedJobOffer.getJobOfferID());

        assertTrue(foundJobOffer.isPresent());
    }

    @Test
    @Rollback
    @Transactional
    public void updateJobOffer(){
        User carrier = userRepository.save(createCarrier());
        User addedCarrier = userRepository.save(carrier);

        JobOffer jobOffer = generateJobOffer(addedCarrier);

        JobOffer addedJobOffer = jobOfferRepository.save(jobOffer);

        Optional<JobOffer> afterAdd = jobOfferRepository.findById(addedJobOffer.getJobOfferID());

        if(afterAdd.isEmpty())
            fail("couldn't fetch job offer");

        String oldTitle = afterAdd.get().getTitleENG();


        afterAdd.get().setTitleENG("new title eng");
        jobOfferRepository.save(addedJobOffer);
        Optional<JobOffer> afterUpdate = jobOfferRepository.findById(afterAdd.get().getJobOfferID());

        if (afterUpdate.isEmpty())
            fail("Couldn't fetch job offer");
        else{
            assertEquals(addedJobOffer.getJobOfferID(), afterUpdate.get().getJobOfferID());
            assertNotEquals(oldTitle, afterUpdate.get().getTitleENG());
        }


    }

    @Test
    @Rollback
    @Transactional
    public void deleteJobOffer(){
        User carrier = userRepository.save(createCarrier());
        User addedCarrier = userRepository.save(carrier);

        JobOffer jobOffer = generateJobOffer(addedCarrier);
        JobOffer addedJobOffer = jobOfferRepository.save(jobOffer);

        Optional<JobOffer> beforeDelete = jobOfferRepository.findById(addedJobOffer.getJobOfferID());
        jobOfferRepository.deleteById(addedJobOffer.getJobOfferID());
        Optional<JobOffer> afterDelete = jobOfferRepository.findById(addedJobOffer.getJobOfferID());

        assertTrue(beforeDelete.isPresent() && afterDelete.isEmpty());
    }

}
