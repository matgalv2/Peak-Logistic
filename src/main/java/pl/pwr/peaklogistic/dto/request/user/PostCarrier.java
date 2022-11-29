package pl.pwr.peaklogistic.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.NIP;
import pl.pwr.peaklogistic.common.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@AllArgsConstructor
public class PostCarrier {

    @Email
    @NotNull
    private String email;

    @NotBlank
    @Length(min = 8, max = 255)
    private String password;

    @Length(min = 5, max = 255)
    private String companyName;

    @Pattern(regexp = "^\\+\\d{1,3}\\s\\d{9,11}$")
    private String phone;

    @NIP
    private String taxIdentificationNumber;

    public static PostCarrier copy(PostCarrier postCarrier, String password) {
//        return new PostCarrier(postCarrier.email, password, postCarrier.userType, postCarrier.companyName, postCarrier.phone, postCarrier.taxIdentificationNumber);
        return new PostCarrier(postCarrier.email, password, postCarrier.companyName, postCarrier.phone, postCarrier.taxIdentificationNumber);
    }

}
