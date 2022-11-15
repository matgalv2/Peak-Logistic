package pl.pwr.peaklogistic.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pwr.peaklogistic.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserSecurityDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userRepository.existsByUsername(username))
            throw new UsernameNotFoundException(username);
        else
            return userRepository.findByUsername(username).get();
    }
}
