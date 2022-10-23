package pl.pwr.peaklogistic.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Customers")
public class Customer implements WebUser {

    @Id
    private Long userID;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn
    private User user;
    private String fullName;
}