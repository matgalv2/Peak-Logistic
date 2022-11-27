package pl.pwr.peaklogistic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.pwr.peaklogistic.dto.request.jobOffer.PutJobOffer;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "JobOffers")
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobOfferID;

    @ManyToOne
    @JoinColumn(name = "carrierID")
    private User carrier;

    @NotBlank
    @Length(max = 255)
    private String title;

    @NotBlank
    private String content;

    @Email
    private String contactEmail;


    public JobOffer update(PutJobOffer jobOffer) {
        title = jobOffer.getTitle();
        content = jobOffer.getContent();
        contactEmail = jobOffer.getContactEmail();
        return this;
    }

}
