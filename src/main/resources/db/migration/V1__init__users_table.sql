CREATE TABLE Users(
    userID BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    userType ENUM('Admin','Carrier', 'Customer') NOT NULL,
    fullName VARCHAR(255),
    companyName VARCHAR(255),
    phone VARCHAR(15),
    taxIdentificationNumber VARCHAR(10),

    PRIMARY KEY (userID)
);