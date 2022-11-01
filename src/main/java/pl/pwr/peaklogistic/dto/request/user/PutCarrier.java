package pl.pwr.peaklogistic.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PutCarrier {
    private String companyName;
    private String phone;
}
