package pl.pwr.peaklogistic.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Customers")
public class Customer {
    @Id
    private Long userID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;
    private String fullName;
}