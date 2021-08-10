CREATE DATABASE IF NOT EXISTS store;
use store;

CREATE TABLE IF NOT EXISTS product
(
    id INT AUTO_INCREMENT,
    name VARCHAR(100),
    price INT,
    creationData DATETIME,
    status VARCHAR(20),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS property
(
    productId INT,
    propertyName VARCHAR(30),
    propertyValue VARCHAR(30),
    CONSTRAINT propertyFK
    FOREIGN KEY (productId) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user
(
    id INT AUTO_INCREMENT,
    login VARCHAR(50) NULL,
    password VARCHAR(50) NULL,
    name VARCHAR(50) NULL,
    role VARCHAR(10) NOT NULL,
    blocked BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bag
(
    userId INT NOT NULL,
    productId INT NOT NULL,
    CONSTRAINT bagFKU
    FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT bagFKP
    FOREIGN KEY (productId) REFERENCES product(id) ON DELETE CASCADE
);