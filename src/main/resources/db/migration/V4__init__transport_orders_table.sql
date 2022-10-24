CREATE TABLE TransportOrders (
    transportOrderID BIGINT NOT NULL AUTO_INCREMENT,
    customerID BIGINT NOT NULL,
    fromLocation VARCHAR(255) NOT NULL,
    toLocation VARCHAR(255) NOT NULL,
    startDateFrom DATE NOT NULL,
    startDateTo DATE NOT NULL,
    endDateFrom DATE NOT NULL,
    endDateTo DATE NOT NULL,
    weight FLOAT NOT NULL,
    height INT NOT NULL,
    width INT NOT NULL,
    depth INT NOT NULL,

    PRIMARY KEY (transportOrderID),
    FOREIGN KEY (customerID) REFERENCES Users(userID)
);