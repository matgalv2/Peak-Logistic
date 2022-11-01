package pl.pwr.peaklogistic.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "Messages")
public class Message {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long messageID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender")
    private User sender;


    @ManyToOne
    @JoinColumn(name = "chatID")
    private Chat chat;

    private String content;
    private Timestamp sentAt;
}