package pl.pwr.peaklogistic.model;


import lombok.Data;
import pl.pwr.peaklogistic.common.UserType;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Email
    private String email;
    private String password;
    private UserType userType;
}