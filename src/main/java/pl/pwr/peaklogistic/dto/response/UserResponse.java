package pl.pwr.peaklogistic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.common.UserType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse{
    private Long userID;
    private String email;
//    private String password;
    private UserType userType;
    private String fullName;
    private String companyName;
    private String phone;
    private String taxIdentificationNumber;

}
