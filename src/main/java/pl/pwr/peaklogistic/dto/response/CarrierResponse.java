package pl.pwr.peaklogistic.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import pl.pwr.peaklogistic.model.User;

@AllArgsConstructor
@Data
public class CarrierResponse extends UserTypeResponse {
    private Long userID;
    private String email;
    private String companyName;
    private String phone;
    private String taxIdentificationNumber;

    public static CarrierResponse fromUser(User user){
        return
                new CarrierResponse(
                        user.getUserID(),
                        user.getEmail(),
                        user.getCompanyName(),
                        user.getPhone(),
                        user.getTaxIdentificationNumber());
    }
}
