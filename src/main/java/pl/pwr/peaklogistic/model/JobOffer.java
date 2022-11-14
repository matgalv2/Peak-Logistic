package pl.pwr.peaklogistic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.dto.request.jobOffer.PostJobOffer;
import pl.pwr.peaklogistic.dto.request.jobOffer.PutJobOffer;

import javax.persistence.*;

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
    //    private Long carrierID;
    private String title;
    private String content;
    private String contactEmail;


    public JobOffer update(PutJobOffer jobOffer) {
        title = jobOffer.getTitle();
        content = jobOffer.getContent();
        contactEmail = jobOffer.getContactEmail();
        return this;
    }

}
