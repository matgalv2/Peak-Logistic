package pl.pwr.peaklogistic.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferResponse {

    private Long jobOfferID;
    private CarrierResponse carrier;
    private String title;
    private String content;
    private String contactEmail;
}
