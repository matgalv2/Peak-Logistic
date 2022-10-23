package pl.pwr.peaklogistic.dto.request.transportOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;


@Getter
@AllArgsConstructor
public class PostTransportOrder {
    private Long customerID;

    private String fromLocation;
    private String toLocation;

    private Date startDateFrom;
    private Date startDateTo;
    private Date endDateFrom;
    private Date endDateTo;

    private float productWeightInKG;
    private int productHeightInCM;
    private int productWidthInCM;
    private int productDepthInCM;
}
