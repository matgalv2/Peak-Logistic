package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
