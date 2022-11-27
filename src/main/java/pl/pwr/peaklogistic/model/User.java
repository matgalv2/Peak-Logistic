package pl.pwr.peaklogistic.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.NIP;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.dto.request.user.PutCarrier;
import pl.pwr.peaklogistic.dto.request.user.PutCustomer;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Email
    @NotNull
    private String email;

    @NotBlank
    @Length(min = 8, max = 255)
    @NotNull
    private String password;

    @NotNull
    private UserType userType;

    @NotBlank
    @Length(min = 5, max = 255)
    @NotNull
    private String nickname;

    @NotBlank
    @Length(min = 5, max = 255)
    @NotNull
    private String companyName;

    @Pattern(regexp = "^\\+\\d{1,3}\\s\\d{9,11}$")
    @NotNull
    private String phone;

    @NIP
    @NotNull
    private String taxIdentificationNumber;


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
    public String getUsername() {
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