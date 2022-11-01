package pl.pwr.peaklogistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;

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

    @ManyToOne
    @JoinColumn(name = "customerID")
    private User customer;

    private String fromLocation;
    private String toLocation;

    private Date startDateFrom;
    private Date startDateTo;
    private Date endDateFrom;
    private Date endDateTo;

    private float productWeightKG;
    private int productHeightCM;
    private int productWidthCM;
    private int productDepthCM;

    @OneToMany(mappedBy = "transportOrder", cascade = CascadeType.ALL)
    private Set<TransportOffer> transportOffers;

}