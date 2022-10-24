package pl.pwr.peaklogistic.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarrierRequest {
    private String companyName;
    private String phone;
}
