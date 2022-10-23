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
    @Transient
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver", referencedColumnName = "userID")
    private User sender;
    private Long senderID;
    @Transient
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender", referencedColumnName = "userID")
    private User receiver;
    private Long receiverID;
    private String content;
    private Timestamp sentAt;
}