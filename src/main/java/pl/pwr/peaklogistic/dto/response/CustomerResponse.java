package pl.pwr.peaklogistic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.pwr.peaklogistic.model.User;

@AllArgsConstructor
@Data
public class CustomerResponse extends UserTypeResponse {
    private Long userID;
    private String email;
    private String fullName;

    public static CustomerResponse fromUser(User user){
        return new CustomerResponse(user.getUserID(), user.getEmail(), user.getFullName());
    }
}
