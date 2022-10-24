package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.TransportOrder;

public interface TransportOrderRepository extends JpaRepository<TransportOrder, Long> {

}