CREATE TABLE TransportOrders
(
    transportOrderID BIGINT       NOT NULL AUTO_INCREMENT,
    customerID       BIGINT       NOT NULL,
    fromLocation     VARCHAR(255) NOT NULL,
    toLocation       VARCHAR(255) NOT NULL,
    startDateFrom    DATE         NOT NULL,
    startDateTo      DATE         NOT NULL,
    endDateFrom      DATE         NOT NULL,
    endDateTo        DATE         NOT NULL,
    productWeightKG  FLOAT        NOT NULL,
    productHeightCM  INT          NOT NULL,
    productWidthCM   INT          NOT NULL,
    productDepthCM   INT          NOT NULL,

    PRIMARY KEY (transportOrderID),
    FOREIGN KEY (customerID) REFERENCES Users (userID)
);