package pl.pwr.peaklogistic.dto.request.transportOffer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.pwr.peaklogistic.common.validators.FloatRange;

import javax.validation.constraints.NotNull;
import java.util.Date;


@AllArgsConstructor
@Getter
public class PostTransportOffer {
    private long transportOrderID;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    @FloatRange(min = 0.0f, twoDecimalPlaces = true, message = "Value cannot be negative neither have more than 2 decimal places")
    private float price;
}
