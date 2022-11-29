package pl.pwr.peaklogistic.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import pl.pwr.peaklogistic.common.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class PostCustomer {

    @Email
    @NotNull
    private String email;

    @NotBlank
    @Length(min = 8, max = 255)
    private String password;

    @Length(min = 5, max = 255)
    private String nickname;

    public static PostCustomer copy(PostCustomer postCustomer, String password) {
        return new PostCustomer(postCustomer.email, password, postCustomer.nickname);
    }


}
