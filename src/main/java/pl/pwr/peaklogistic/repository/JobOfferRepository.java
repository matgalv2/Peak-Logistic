package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.JobOffer;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
}
