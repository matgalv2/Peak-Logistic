package pl.pwr.peaklogistic.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatID;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "carrierID")
    private User carrier;

    private Timestamp createdAt;

    @OneToMany(mappedBy = "chat")
    private Set<Message> messages;


}
