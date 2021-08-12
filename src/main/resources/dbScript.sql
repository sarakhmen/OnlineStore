CREATE DATABASE IF NOT EXISTS store;
use store;

CREATE TABLE IF NOT EXISTS product
(
    id INT AUTO_INCREMENT,
    name VARCHAR(100),
    price INT,
    creationDate DATETIME,
    status VARCHAR(20) NULL,
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
    role VARCHAR(10) DEFAULT 'USER',
    blocked BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bag
(
    orderId INT AUTO_INCREMENT,
    userId INT NOT NULL,
    productId INT NOT NULL,
    PRIMARY KEY (orderId),
    CONSTRAINT bagFKU
    FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT bagFKP
    FOREIGN KEY (productId) REFERENCES product(id) ON DELETE CASCADE
);


INSERT INTO product(name, price, creationDate) values ('Telephone', 1000, NOW());
INSERT INTO product(name, price, creationDate) values ('TV', 1500, NOW());
INSERT INTO product(name, price, creationDate) values ('CAR', 13000, NOW());
INSERT INTO product(name, price, creationDate) values ('Something else', 100, NOW());

INSERT INTO property(productId, propertyName, propertyValue) values (1, 'Model', 'iPhone X');
INSERT INTO property(productId, propertyName, propertyValue) values (1, 'Color', 'white');
INSERT INTO property(productId, propertyName, propertyValue) values (3, 'Model', 'Subaru Forester');

INSERT INTO user(login, password, name, role) values ('qwer', '12345', 'first user', 'USER');
