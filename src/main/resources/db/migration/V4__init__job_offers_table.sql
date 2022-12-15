CREATE TABLE JobOffers
(
    jobOfferID   BIGINT       NOT NULL AUTO_INCREMENT,
    carrierID    BIGINT       NOT NULL,
    titlePL      VARCHAR(255) NOT NULL,
    titleENG     VARCHAR(255),
    contentPL    TEXT         NOT NULL,
    contentENG   TEXT,
    contactEmail VARCHAR(255),

    PRIMARY KEY (jobOfferID),
    FOREIGN KEY (carrierID) REFERENCES Users (userID)
);