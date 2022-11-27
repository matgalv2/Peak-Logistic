package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.JobOffer;

import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> findAllByCarrierUserID(long id);
}
