package pl.pwr.peaklogistic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.User;

@Data
@AllArgsConstructor
public class UserResponse extends UserTypeResponse {
    private Long userID;
    private String email;
    private UserType userType;
    private String fullName;
    private String companyName;
    private String phone;
    private String taxIdentificationNumber;

    public static UserResponse fromUser(User user){
        return
                new UserResponse(
                        user.getUserID(),
                        user.getEmail(),
                        user.getUserType(),
                        user.getFullName(),
                        user.getCompanyName(),
                        user.getPhone(),
                        user.getTaxIdentificationNumber()
                        );
    }

    public static UserTypeResponse toAPI(User user){
        if(user.getUserType() == UserType.Carrier)
            return CarrierResponse.fromUser(user);
        else if(user.getUserType() == UserType.Customer)
            return CustomerResponse.fromUser(user);
        else
            return UserResponse.fromUser(user);
    }
}
