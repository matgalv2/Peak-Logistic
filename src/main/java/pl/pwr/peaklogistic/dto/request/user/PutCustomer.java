package pl.pwr.peaklogistic.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class PutCustomer {

    @Length(min = 5, max = 255)
    private String nickname;
}
