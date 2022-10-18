package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.OfferSpecification;

public interface OfferSpecificationRepository extends JpaRepository<OfferSpecification, Long> {
}