package pl.pwr.peaklogistic.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.user.PutCarrier;
import pl.pwr.peaklogistic.dto.request.user.PutCustomer;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    //    @Email
    private String username;
    private String password;
    private UserType userType;
    private String fullName;
    private String companyName;
    private String phone;
    private String taxIdentificationNumber;
    

//    @JsonIgnore
//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<TransportOrder> transportOrders;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<TransportOffer> transportOffers;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<JobOffer> jobOffers;


    public void updateFromCustomerRequest(PutCustomer putCustomer) {
        fullName = putCustomer.getFullName();
    }

    public void updateFromCarrierRequest(PutCarrier putCarrier) {
        companyName = putCarrier.getCompanyName();
        phone = putCarrier.getPhone();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.toString().toUpperCase()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}