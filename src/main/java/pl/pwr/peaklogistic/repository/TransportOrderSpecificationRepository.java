package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.TransportOrderSpecification;

public interface TransportOrderSpecificationRepository extends JpaRepository<TransportOrderSpecification, Long> {
}