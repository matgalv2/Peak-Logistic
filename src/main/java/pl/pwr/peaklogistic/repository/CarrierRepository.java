package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.Carrier;


public interface CarrierRepository extends JpaRepository<Carrier, Long> {

}
