package pl.pwr.peaklogistic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private float price;

    //TODO: walidacja dat

}