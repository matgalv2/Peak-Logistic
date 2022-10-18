package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);
    User findByEmail(String email);

}
