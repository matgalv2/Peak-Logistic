package pl.pwr.peaklogistic.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportOfferResponse {

    private Long transportOfferID;
    private CarrierResponse carrier;

    private Date startDate;
    private Date endDate;
    private float price;
}
