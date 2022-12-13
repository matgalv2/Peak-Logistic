package pl.pwr.peaklogistic.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PutCarrier {

    @Length(min = 5, max = 255)
    private String companyName;

    @Pattern(regexp = "^\\+\\d{1,3}\\s\\d{9,11}$")
    private String phone;
}
