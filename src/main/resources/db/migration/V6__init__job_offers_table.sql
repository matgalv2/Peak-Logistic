CREATE TABLE JobOffers (
    jobOfferID INT NOT NULL AUTO_INCREMENT,
    carrierID INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    contactEmail VARCHAR(255),

    PRIMARY KEY (jobOfferID),
    FOREIGN KEY (carrierID) REFERENCES Users(userID)
);