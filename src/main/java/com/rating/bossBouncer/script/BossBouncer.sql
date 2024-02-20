CREATE TABLE users (
    UserID BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    imageUrl VARCHAR(255),
    emailVerified BOOLEAN NOT NULL DEFAULT false,
    password VARCHAR(255),
    provider VARCHAR(50) NOT NULL,
    providerId VARCHAR(255)
);
CREATE TABLE boss (
    BossID BIGINT PRIMARY KEY AUTO_INCREMENT,
    First_Name VARCHAR(255),
    Last_Name VARCHAR(255),
    Email_Id VARCHAR(255),
    Title VARCHAR(255),
    Company VARCHAR(255),
    Department VARCHAR(255)
);

CREATE TABLE rating (
    RatingID BIGINT PRIMARY KEY AUTO_INCREMENT,
    UserID BIGINT,
    BossID BIGINT,
    Rating VARCHAR(50),
    Timestamp DATETIME,
    CONSTRAINT fk_user FOREIGN KEY (UserID) REFERENCES users (UserID),
    CONSTRAINT fk_boss FOREIGN KEY (BossID) REFERENCES boss (BossID)
);

CREATE TABLE payment (
    PaymentID BIGINT PRIMARY KEY AUTO_INCREMENT,
    UserID BIGINT,
    Amount DECIMAL(10, 2),
    Timestamp DATETIME,
    CONSTRAINT fk_user FOREIGN KEY (UserID) REFERENCES users (UserID)
);
