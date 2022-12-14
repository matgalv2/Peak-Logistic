package pl.pwr.peaklogistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.pwr.peaklogistic.common.validators.FloatRange;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private boolean completed;

    @NotBlank
    @Length(max = 255)
    private String fromLocation;

    @NotBlank
    @Length(max = 255)
    private String toLocation;

    @NotNull
    private Date startDateFrom;

    @NotNull
    private Date startDateTo;

    @NotNull
    private Date endDateFrom;

    @NotNull
    private Date endDateTo;

    @NotNull
    @FloatRange(min = 0.0f)
    private float productWeightKG;

    @NotNull
    @Min(1)
    private int productHeightCM;

    @NotNull
    @Min(1)
    private int productWidthCM;

    @NotNull
    @Min(1)
    private int productDepthCM;

    @OneToMany(mappedBy = "transportOrderID", cascade = CascadeType.ALL)
    private Set<TransportOffer> transportOffers;

}