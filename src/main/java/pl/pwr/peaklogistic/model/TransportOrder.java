package pl.pwr.peaklogistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.dto.request.TransportOrderRequest;

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

    private float productWeightKG;
    private int productHeightCM;
    private int productWidthCM;
    private int productDepthCM;

    @OneToMany(mappedBy = "transportOrder", cascade = CascadeType.ALL)
    private Set<TransportOffer> transportOffers;

    public static TransportOrder fromRequest(TransportOrderRequest transportOrderRequest, User customer){

        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setCustomer(customer);
        transportOrder.setFromLocation(transportOrderRequest.getFromLocation());
        transportOrder.setToLocation(transportOrderRequest.getToLocation());
        transportOrder.setStartDateFrom(transportOrderRequest.getStartDateFrom());
        transportOrder.setStartDateTo(transportOrderRequest.getStartDateTo());
        transportOrder.setEndDateFrom(transportOrderRequest.getEndDateFrom());
        transportOrder.setEndDateTo(transportOrderRequest.getEndDateTo());
        transportOrder.setProductWeightKG(transportOrderRequest.getProductWeightInKG());
        transportOrder.setProductHeightCM(transportOrderRequest.getProductHeightInCM());
        transportOrder.setProductWidthCM(transportOrderRequest.getProductWidthInCM());
        transportOrder.setProductDepthCM(transportOrderRequest.getProductDepthInCM());

        return transportOrder;
    }


}