CREATE TABLE Messages
(
    messageID BIGINT    NOT NULL AUTO_INCREMENT,
    sender    BIGINT    NOT NULL,
    chatID    BIGINT    NOT NULL,
    content   TINYTEXT  NOT NULL,
    sentAt    TIMESTAMP NOT NULL,

    PRIMARY KEY (messageID),
    FOREIGN KEY (sender) REFERENCES Users (userID),
    FOREIGN KEY (chatID) REFERENCES Chats (chatID)
);