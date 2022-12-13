package pl.pwr.peaklogistic.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserPasswordRequest {
    @NotBlank
    @Length(min = 5, max = 255)
    private String oldPassword;

    @NotBlank
    @Length(min = 5, max = 255)
    private String newPassword;

    @NotBlank
    @Length(min = 5, max = 255)
    private String repeatedNewPassword;
}
