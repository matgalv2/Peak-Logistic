package pl.pwr.peaklogistic.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "Offer")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carrier", referencedColumnName = "userID")
    private Carrier carrier;
    private String startingLoc;
    private String destination;
    private LocalDateTime time;

}