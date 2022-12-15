package pl.pwr.peaklogistic.dto.request.jobOffer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostJobOffer {

    @NotBlank
    @Length(max = 255)
    private String titlePL;

    @NotBlank
    @Length(max = 255)
    private String titleENG;

    @NotBlank
    @Length(max = 255)
    private String contentPL;

    @NotBlank
    @Length(max = 255)
    private String contentENG;

    @Email
    private String contactEmail;
}
