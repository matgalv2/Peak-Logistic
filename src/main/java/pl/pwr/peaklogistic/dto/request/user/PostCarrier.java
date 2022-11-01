package pl.pwr.peaklogistic.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.pwr.peaklogistic.common.UserType;



@Data
@AllArgsConstructor
public class PostCarrier {
    private String email;
    private String password;
    private UserType userType;
    private String companyName;
    private String phone;
    private String taxIdentificationNumber;

    public static PostCarrier copy(PostCarrier postCarrier, String password){
        return new PostCarrier(postCarrier.email, password, postCarrier.userType, postCarrier.companyName, postCarrier.phone, postCarrier.taxIdentificationNumber);
    }

}
