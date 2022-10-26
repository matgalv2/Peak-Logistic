package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.TransportOffer;

public interface TransportOfferRepository extends JpaRepository<TransportOffer, Long> {
}