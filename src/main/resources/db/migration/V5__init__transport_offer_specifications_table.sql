CREATE TABLE TransportOrderSpecifications (
    transportOrderSpecificationID BIGINT NOT NULL AUTO_INCREMENT,
    transportOrderID BIGINT NOT NULL,
    carrierID BIGINT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    price FLOAT NOT NULL,

    PRIMARY KEY (transportOrderSpecificationID),
    FOREIGN KEY (transportOrderID) REFERENCES TransportOrders(transportOrderID),
    FOREIGN KEY (carrierID) REFERENCES Users(userID)
);