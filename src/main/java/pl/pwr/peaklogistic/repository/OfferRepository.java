package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}