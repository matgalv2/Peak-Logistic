package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.TransportOrder;

import java.util.List;

public interface TransportOrderRepository extends JpaRepository<TransportOrder, Long> {
    List<TransportOrder> getTransportOrdersByCustomerUserID(long id);
}