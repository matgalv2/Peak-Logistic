package pl.pwr.peaklogistic.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportOrderResponse {

    private Long transportOrderID;
    private CustomerResponse customer;

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

    private Set<TransportOfferResponse> transportOffers;
}
