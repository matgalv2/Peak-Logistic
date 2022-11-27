package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pwr.peaklogistic.model.TransportOrder;

import java.util.List;

public interface TransportOrderRepository extends JpaRepository<TransportOrder, Long> {
    List<TransportOrder> getTransportOrdersByCustomerUserID(long id);

//    @Query(
//            value = "SELECT * FROM TransportOffers to WHERE to = :id"
//    )
//    List<TransportOrder> getTransportOrdersByTransportOffersCarrierID(@Param("id") long id);


    @Override
    List<TransportOrder> findAllById(Iterable<Long> longs);

}