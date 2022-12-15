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
    private String titlePL;
    private String titleENG;
    private String contentPL;
    private String contentENG;
    private String contactEmail;
}
