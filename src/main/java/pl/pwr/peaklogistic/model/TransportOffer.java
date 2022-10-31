package pl.pwr.peaklogistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TransportOffers")
public class TransportOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportOfferID;
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


    public static TransportOffer fromRequest(PostTransportOffer postTransportOffer, TransportOrder transportOrder, User carrier){
        TransportOffer transportOffer = new TransportOffer();
        transportOffer.transportOrder = transportOrder;
        transportOffer.carrier = carrier;
        transportOffer.startDate = postTransportOffer.getStartDate();
        transportOffer.endDate = postTransportOffer.getEndDate();
        transportOffer.price = transportOffer.getPrice();

        return transportOffer;
    }

}