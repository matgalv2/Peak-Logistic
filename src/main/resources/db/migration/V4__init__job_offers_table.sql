CREATE TABLE JobOffers
(
    jobOfferID   BIGINT       NOT NULL AUTO_INCREMENT,
    carrierID    BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    content      TEXT         NOT NULL,
    contactEmail VARCHAR(255),

    PRIMARY KEY (jobOfferID),
    FOREIGN KEY (carrierID) REFERENCES Users (userID)
);