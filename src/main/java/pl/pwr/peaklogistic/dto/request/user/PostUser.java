package pl.pwr.peaklogistic.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PostUser {

    @Email
    @NotNull
    private String email;

    @NotBlank
    @Length(min = 5, max = 255)
    private String password;

    public static PostUser copy(PostUser postUser, String password) {
        return new PostUser(postUser.email, password);
    }
}
