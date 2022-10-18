package pl.pwr.peaklogistic.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "OfferSpecification")
public class OfferSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerSpecificationID;
    @ManyToOne
    @JoinColumn(name = "customer", referencedColumnName = "userID")
    private Customer customer;
    private String start;
    private String destination;
    private LocalDateTime time;


}