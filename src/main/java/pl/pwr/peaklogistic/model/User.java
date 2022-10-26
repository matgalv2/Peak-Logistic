package pl.pwr.peaklogistic.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.CarrierRequest;
import pl.pwr.peaklogistic.dto.request.CustomerRequest;

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

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<TransportOrder> transportOrders;

    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL)
    private Set<TransportOffer> transportOffers;

    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL)
    private Set<JobOffer> jobOffers;


    public void updateFromCustomerRequest(CustomerRequest customerRequest){
        fullName = customerRequest.getFullName();
    }

    public void updateFromCarrierRequest(CarrierRequest carrierRequest){
        companyName = carrierRequest.getCompanyName();
        phone = carrierRequest.getPhone();
    }

}