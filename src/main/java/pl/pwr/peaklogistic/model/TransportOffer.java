package pl.pwr.peaklogistic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "transportOrderID")
    private TransportOrder transportOrder;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "carrierID")
    private User carrier;

    private Date startDate;
    private Date endDate;
    private float price;

}