package pl.pwr.peaklogistic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.pwr.peaklogistic.model.Message;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
public class PostMessage {
    private String content;
    private long senderID;
    private long receiverID;
    private Timestamp sentAt;


}
