package pl.pwr.peaklogistic.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.nullability.MaybeNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.NIP;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.user.PutCarrier;
import pl.pwr.peaklogistic.dto.request.user.PutCustomer;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Email
    private String email;
    @NotBlank
    @Length(min = 8)
    private String password;
    @NotNull
    private UserType userType;

    @NotBlank
    @Length(min = 5)
    private String nickname;
    @NotBlank

    private String companyName;
    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")
    private String phone;
    @NIP
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
        nickname = putCustomer.getFullName();
    }

    public void updateFromCarrierRequest(PutCarrier putCarrier) {
        companyName = putCarrier.getCompanyName();
        phone = putCarrier.getPhone();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.toString().toUpperCase()));
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername(){
        return email;
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