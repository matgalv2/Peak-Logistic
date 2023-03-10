package pl.pwr.peaklogistic.config;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.pwr.peaklogistic.services.UserSecurityDetailsService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@AllArgsConstructor
@Configuration
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserSecurityDetailsService userSecurityDetailsService;
//    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/login");

        http.cors().and().csrf().disable();
//        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        /* All users */

        http.authorizeRequests().antMatchers(GET, "/job-offers", "/job-offers/**", "/carriers/{id}/job-offers").permitAll();
        http.authorizeRequests().antMatchers(POST, "/login", "/customers", "/carriers").permitAll();


        /* Signed users */

        http.authorizeRequests().antMatchers(GET, "/users/{id}")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(PUT, "/users/{id}")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(PUT, "/users/{id}/pwd")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(GET, "/carriers")
                .access("@accessGuard.loggedUser(authentication)");

        /* Customer */

        http.authorizeRequests().antMatchers(GET, "/transport-orders/{id}")
                .access("@accessGuard.checkOrderByOrderId(authentication, #id)");
        http.authorizeRequests().antMatchers(GET, "/customers/{id}/transport-orders")
                .access("@accessGuard.checkOrderByCustomerId(authentication, #id)");
        http.authorizeRequests().antMatchers(POST, "/customers/{id}/transport-orders")
                .access("@accessGuard.checkOrderByCustomerId(authentication, #id)");
        http.authorizeRequests().antMatchers(DELETE, "/transport-orders/{id}")
                .access("@accessGuard.checkOrderByOrderId(authentication, #id)");
        http.authorizeRequests().antMatchers(PATCH, "/transport-orders/{id}")
                .access("@accessGuard.checkOrderByOrderId(authentication, #id)");

        /** update **/

        http.authorizeRequests().antMatchers(PUT, "/customers/{id}")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        /* Carrier */

        http.authorizeRequests().antMatchers(GET, "/transport-offers/{id}")
                .access("@accessGuard.checkOfferByOfferId(authentication, #id)");
        http.authorizeRequests().antMatchers(GET, "/carriers/{id}/transport-offers")
                .access("@accessGuard.checkOffersByCarrierId(authentication, #id)");
        http.authorizeRequests().antMatchers(POST, "/carriers/{id}/transport-offers")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");
        http.authorizeRequests().antMatchers(DELETE, "/transport-offers/{id}")
                .access("@accessGuard.checkOfferByOfferId(authentication, #id)");


        http.authorizeRequests().antMatchers(POST, "/carriers/{id}/job-offers")
                .access("@accessGuard.checkJobOfferByCarrierId(authentication, #id)");
        http.authorizeRequests().antMatchers(PUT, "/job-offers/{id}")
                .access("@accessGuard.checkJobOfferByJobOfferId(authentication, #id)");
        http.authorizeRequests().antMatchers(DELETE, "/job-offers/{id}")
                .access("@accessGuard.checkJobOfferByJobOfferId(authentication, #id)");


        http.authorizeRequests().antMatchers(GET, "/carriers/{id}/transport-orders")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");

        /** update **/

        http.authorizeRequests().antMatchers(PUT, "/carriers/{id}")
                .access("@accessGuard.checkUserByUserId(authentication, #id)");

        /* Admin */

        http.authorizeRequests().antMatchers(GET, "/users", "/customers").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/users/{id}").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/admins").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/transport-offers").hasAuthority("ADMIN");


        /* Custom access*/
        http.authorizeRequests().antMatchers(GET, "/transport-orders/{id}")
                .access("@accessGuard.checkOrderByRoleOrOrderId(authentication, #id)");

        http.authorizeRequests().antMatchers(GET, "/transport-orders")
                .access("@accessGuard.carrierOrAdmin(authentication)");


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
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration().applyPermitDefaultValues();
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedHeaders(List.of("**"));
//        configuration.setAllowedOriginPatterns(List.of("**"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("**", configuration);
//        return source;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
