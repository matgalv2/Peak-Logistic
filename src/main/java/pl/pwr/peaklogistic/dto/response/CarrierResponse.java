package pl.pwr.peaklogistic.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarrierResponse {
    private Long userID;
    private String email;
    private String companyName;
    private String phone;
    private String taxIdentificationNumber;

}
