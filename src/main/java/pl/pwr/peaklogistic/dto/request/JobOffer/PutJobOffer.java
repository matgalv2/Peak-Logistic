package pl.pwr.peaklogistic.dto.request.JobOffer;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PutJobOffer {
    private String title;
    private String content;
    private String contactEmail;
}
