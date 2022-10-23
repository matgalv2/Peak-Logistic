CREATE TABLE Chats(
    chatID INT NOT NULL AUTO_INCREMENT,
    firstUserID INT NOT NULL,
    secondUserID INT NOT NULL,
    createdAt TIMESTAMP NOT NULL,

    PRIMARY KEY (chatID),
    FOREIGN KEY (firstUserID) REFERENCES Users(userID),
    FOREIGN KEY (secondUserID) REFERENCES Users(userID)
);