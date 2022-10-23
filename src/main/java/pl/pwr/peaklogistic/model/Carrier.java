package pl.pwr.peaklogistic.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "Carriers")
public class Carrier implements WebUser {
    @Id
    private Long userID;
    @Transient
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;
    private String fullName;
    private String taxIdentificationNumber;
    @Email
    private String contactEmail;
    private String phoneNumber;
}