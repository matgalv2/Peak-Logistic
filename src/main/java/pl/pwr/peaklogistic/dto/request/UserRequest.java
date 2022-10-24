package pl.pwr.peaklogistic.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.User;

@Getter
@AllArgsConstructor
public class UserRequest {
    private String email;
    private String password;
    private UserType userType;

    public static User toDomain(UserRequest userRequest){
        User transformed = new User();
        transformed.setEmail(userRequest.email);
        transformed.setPassword(userRequest.password);
        transformed.setUserType(userRequest.userType);

        return transformed;
    }

}
