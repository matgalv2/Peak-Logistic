package pl.pwr.peaklogistic.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "Carriers")
public class Carrier {
    @Id
    private Long userID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;
    private String fullName;
    private String taxIdentificationNumber;
    @Email
    private String contactEmail;
    private String phoneNumber;
}