package pl.pwr.peaklogistic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.dto.request.JobOffer.PostJobOffer;

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

    public static JobOffer fromPostRequest(PostJobOffer postJobOffer, User carrier){
        JobOffer jobOffer = new JobOffer();

        jobOffer.carrier = carrier;
        jobOffer.title = postJobOffer.getTitle();
        jobOffer.content = postJobOffer.getContent();
        jobOffer.contactEmail = postJobOffer.getContactEmail();

        return jobOffer;
    }

}
