package pl.pwr.peaklogistic.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.pwr.peaklogistic.common.UserType;


@Data
@AllArgsConstructor
public class PostCustomer {
    private String email;
    private String password;
    //    private UserType userType;
    private String fullName;

    public static PostCustomer copy(PostCustomer postCustomer, String password) {
        return new PostCustomer(postCustomer.email, password, postCustomer.fullName);
    }


}
