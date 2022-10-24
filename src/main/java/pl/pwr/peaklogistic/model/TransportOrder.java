package pl.pwr.peaklogistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "TransportOrders")
public class TransportOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportOrderID;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customerID")
//    private User user;
    @ManyToOne
    @JoinColumn(name = "customerID")
    private User customer;

    private String fromLocation;
    private String toLocation;

    private Date startDateFrom;
    private Date startDateTo;
    private Date endDateFrom;
    private Date endDateTo;

    private float weight;
    private int height;
    private int width;
    private int depth;

    @OneToMany(mappedBy = "transportOrder")
    private Set<TransportOrderSpecification> transportOrderSpecifications;
}