package pl.pwr.peaklogistic.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.user.PutCarrier;
import pl.pwr.peaklogistic.dto.request.user.PutCustomer;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
//    @Email
    private String email;
    private String password;
    private UserType userType;
    private String fullName;
    private String companyName;
    private String phone;
    private String taxIdentificationNumber;

    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    private Set<Chat> customerChats;

    @OneToMany(mappedBy = "carrier", orphanRemoval = true)
    private Set<Chat> carrierChats;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TransportOrder> transportOrders;

    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TransportOffer> transportOffers;

    @JsonIgnore
    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobOffer> jobOffers;


    public void updateFromCustomerRequest(PutCustomer putCustomer){
        fullName = putCustomer.getFullName();
    }

    public void updateFromCarrierRequest(PutCarrier putCarrier){
        companyName = putCarrier.getCompanyName();
        phone = putCarrier.getPhone();
    }



}