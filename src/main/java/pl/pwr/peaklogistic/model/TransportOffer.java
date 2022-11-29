package pl.pwr.peaklogistic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.common.validators.FloatRange;

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
    @FloatRange(min = 0.0f, twoDecimalPlaces = true, message = "Value cannot be negative neither have more than 2 decimal places")
    private float price;

}