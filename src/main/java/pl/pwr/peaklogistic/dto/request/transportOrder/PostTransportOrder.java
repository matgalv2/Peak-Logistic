package pl.pwr.peaklogistic.dto.request.transportOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import pl.pwr.peaklogistic.common.validators.FloatRange;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;


@Getter
@AllArgsConstructor
public class PostTransportOrder {

    @NotNull
    private boolean completed;

    @NotBlank
    @Length(max = 255)
    private String fromLocation;

    @NotBlank
    @Length(max = 255)
    private String toLocation;

    @NotNull
    private Date startDateFrom;

    @NotNull
    private Date startDateTo;

    @NotNull
    private Date endDateFrom;

    @NotNull
    private Date endDateTo;

    @NotNull
    @FloatRange(min = 0.0f)
    private float productWeightInKG;

    @NotNull
    @Min(1)
    private int productHeightInCM;

    @NotNull
    @Min(1)
    private int productWidthInCM;

    @NotNull
    @Min(1)
    private int productDepthInCM;
}
