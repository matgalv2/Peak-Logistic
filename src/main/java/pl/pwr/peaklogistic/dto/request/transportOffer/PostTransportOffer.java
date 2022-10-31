package pl.pwr.peaklogistic.dto.request.transportOffer;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@AllArgsConstructor
@Getter
public class PostTransportOffer {
    private long transportOrderID;
    private long carrierID;
    private Date startDate;
    private Date endDate;
    private float price;
}
