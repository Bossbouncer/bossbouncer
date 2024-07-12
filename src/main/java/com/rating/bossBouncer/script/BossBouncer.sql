CREATE TABLE boss (
    BossID BIGINT AUTO_INCREMENT PRIMARY KEY,
    First_Name VARCHAR(255) NOT NULL,
    Last_Name VARCHAR(255),
    Email VARCHAR(255) NOT NULL UNIQUE,
    Title VARCHAR(255) NOT NULL,
    Organization VARCHAR(255) NOT NULL,
    Department VARCHAR(255),
    isVerified BOOLEAN NOT NULL DEFAULT false,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(255),
    updatedBy VARCHAR(255)
);

CREATE TABLE payment (
    PaymentID BIGINT AUTO_INCREMENT PRIMARY KEY,
    UserID BIGINT,
    Amount DECIMAL(19, 2),
    Timestamp TIMESTAMP,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(255),
    updatedBy VARCHAR(255),
    FOREIGN KEY (UserID) REFERENCES users(UserID)
);
CREATE TABLE rating (
    RatingID BIGINT AUTO_INCREMENT PRIMARY KEY,
    UserID BIGINT,
    BossID BIGINT,
    Rating VARCHAR(255),
    Status VARCHAR(255),
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(255),
    updatedBy VARCHAR(255),
    FOREIGN KEY (UserID) REFERENCES users(UserID),
    FOREIGN KEY (BossID) REFERENCES boss(BossID)
);
CREATE TABLE users (
    UserID BIGINT AUTO_INCREMENT PRIMARY KEY,
    First_Name VARCHAR(255) NOT NULL,
    Last_Name VARCHAR(255),
    Email VARCHAR(255) NOT NULL UNIQUE,
    isVerified BOOLEAN NOT NULL DEFAULT false,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(255),
    updatedBy VARCHAR(255)
);
