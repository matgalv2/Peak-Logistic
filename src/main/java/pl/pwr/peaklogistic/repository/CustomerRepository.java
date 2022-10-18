package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}