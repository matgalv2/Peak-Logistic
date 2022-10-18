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
    @JoinColumn(name = "receiver", referencedColumnName = "userID")
    private User sender;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender", referencedColumnName = "userID")
    private User receiver;
    private String content;
    private Timestamp sentAt;
}