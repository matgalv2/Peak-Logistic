package pl.pwr.peaklogistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TransportOrderSpecifications")
public class TransportOrderSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportOrderSpecificationID;
    @ManyToOne
    @JoinColumn(name = "transportOrderID")
    private TransportOrder transportOrder;
//    private Long transportOfferID;
    @ManyToOne
    @JoinColumn(name = "carrierID")
    private User carrier;
//    private Long carrierID;
    private Date startDate;
    private Date endDate;
    private float price;


}