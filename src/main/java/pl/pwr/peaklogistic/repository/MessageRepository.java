package pl.pwr.peaklogistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.peaklogistic.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}