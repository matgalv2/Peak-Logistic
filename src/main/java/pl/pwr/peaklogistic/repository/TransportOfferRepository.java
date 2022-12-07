package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.TransportOffer;

import java.util.List;

public interface TransportOfferRepository extends JpaRepository<TransportOffer, Long> {
    List<TransportOffer> getTransportOffersByCarrierUserID(long id);
}