package pl.pwr.peaklogistic.config;


import lombok.AllArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.pwr.peaklogistic.services.UserSecurityDetailsService;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@AllArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserSecurityDetailsService userSecurityDetailsService;
//    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        /* All users */

        http.authorizeRequests().antMatchers(GET, "/job-offers", "/job-offers/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/login", "/signup").permitAll();


        /* Only for testing*/

        /* Signed users */

        http.authorizeRequests().antMatchers(GET, "/users/{id}")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(PUT, "/users/{id}")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");

        /* Customer */

        http.authorizeRequests().antMatchers(GET, "/transport-orders/{id}")
                .access("@accessGuard.checkOrderByOrderId(authentication, #id)");
        http.authorizeRequests().antMatchers(GET, "/customer/{id}/transport-orders")
                .access("@accessGuard.checkOrdersByCustomerId(authentication, #id)");
        http.authorizeRequests().antMatchers(POST, "/customer/{id}/transport-orders")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(DELETE, "/transport-orders/{id}")
                .access("@accessGuard.checkOrderByCustomerId(authentication, #id)");

        /* Carrier */

        http.authorizeRequests().antMatchers(GET, "/transport-offers/{id}")
                .access("@accessGuard.checkOfferByOfferId(authentication, #id)");
        http.authorizeRequests().antMatchers(GET, "/carriers/{id}/transport-offers")
                .access("@accessGuard.checkOffersByCarrierId(authentication, #id)");
        http.authorizeRequests().antMatchers(POST, "/carriers/{id}/transport-offers")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(DELETE, "/transport-offers/{id}")
                .access("@accessGuard.checkOfferByCarrierId(authentication, #id)");


        http.authorizeRequests().antMatchers(POST, "/carriers/{id}/job-offers")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(PUT, "/job-offers/{id}")
                .access("@accessGuard.checkJobOfferByJobOfferId(authentication, #id)");
        http.authorizeRequests().antMatchers(DELETE, "/job-offers/{id}")
                .access("@accessGuard.checkJobOfferByCarrierId(authentication, #id)");

        /* Admin */
        http.authorizeRequests().antMatchers("/users", "/users/{id}", "/admins").hasAuthority("ADMIN");
        // to przenieść z admina
        http.authorizeRequests()
                .antMatchers(GET, "/transport-orders", "/transport-orders/**")
                .hasAuthority("ADMIN")
                .antMatchers();


        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityDetailsService);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
