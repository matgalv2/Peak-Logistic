CREATE TABLE TransportOffers (
    transportOfferID BIGINT NOT NULL AUTO_INCREMENT,
    transportOrderID BIGINT NOT NULL,
    carrierID BIGINT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    price FLOAT NOT NULL,

    PRIMARY KEY (transportOfferID),
    FOREIGN KEY (transportOrderID) REFERENCES TransportOrders(transportOrderID),
    FOREIGN KEY (carrierID) REFERENCES Users(userID)
);