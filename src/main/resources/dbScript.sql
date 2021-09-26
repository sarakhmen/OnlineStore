CREATE DATABASE IF NOT EXISTS store;
use store;

CREATE TABLE IF NOT EXISTS product
(
    id INT AUTO_INCREMENT,
    name VARCHAR(100),
    price DOUBLE,
    creationDate DATETIME,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS property
(
    id INT AUTO_INCREMENT,
    productId INT,
    propertyName VARCHAR(30),
    propertyValue VARCHAR(30),
    PRIMARY KEY (id),
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
    id INT AUTO_INCREMENT,
    userId INT NOT NULL,
    productId INT NOT NULL,
    status VARCHAR(20) DEFAULT 'Unregistered',
    PRIMARY KEY (id),
    CONSTRAINT bagFKU
    FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT bagFKP
    FOREIGN KEY (productId) REFERENCES product(id) ON DELETE CASCADE
);


INSERT INTO product(name, price, creationDate) values ('Telephone', 1000, NOW());
INSERT INTO product(name, price, creationDate) values ('TV', 1500, NOW());
INSERT INTO product(name, price, creationDate) values ('Car', 13000, NOW());
INSERT INTO product(name, price, creationDate) values ('Washer', 800, NOW());
INSERT INTO product(name, price, creationDate) values ('Blanked', 100, NOW());
INSERT INTO product(name, price, creationDate) values ('Computer', 1200, NOW());
INSERT INTO product(name, price, creationDate) values ('Schoolbag', 120, NOW());
INSERT INTO product(name, price, creationDate) values ('Sofa', 500, NOW());
INSERT INTO product(name, price, creationDate) values ('Vacuum', 300, NOW());
INSERT INTO product(name, price, creationDate) values ('Fridge', 600, NOW());
INSERT INTO product(name, price, creationDate) values ('Iron', 150, NOW());
INSERT INTO product(name, price, creationDate) values ('Kettle', 50, NOW());


INSERT INTO property(productId, propertyName, propertyValue)
    values (1, 'Model', 'pixel4');

INSERT INTO property(productId, propertyName, propertyValue)
values (1, 'Color', 'grey');


INSERT INTO property(productId, propertyName, propertyValue)
values (2, 'Model', 'LG');

INSERT INTO property(productId, propertyName, propertyValue)
values (2, 'Size', 'big');

INSERT INTO property(productId, propertyName, propertyValue)
values (3, 'Model', 'audi a6');

INSERT INTO property(productId, propertyName, propertyValue)
values (3, 'Size', 'very big');


INSERT INTO property(productId, propertyName, propertyValue)
values (3, 'Color', 'green');


INSERT INTO user(login, password, name, role) values ('qwerty@google.com', '12345', 'First User','USER');
INSERT INTO user(login, password, name, role) values ('artur.sarahman@gmail.com', 'qwerty', 'Artur Sarakhman','ADMIN');

# INSERT INTO bag(userId, productId) values(1, 1);
# INSERT INTO bag(userId, productId) values(1, 2);
# INSERT INTO bag(userId, productId) values(1, 3);
# INSERT INTO bag(userId, productId) values(1, 4);
# INSERT INTO bag(userId, productId) values(1, 5);
# INSERT INTO bag(userId, productId) values(1, 6);
# INSERT INTO bag(userId, productId) values(1, 7);
# INSERT INTO bag(userId, productId) values(2, 8);
# INSERT INTO bag(userId, productId) values(2, 9);
# INSERT INTO bag(userId, productId) values(2, 10);
# INSERT INTO bag(userId, productId) values(2, 11);
# INSERT INTO bag(userId, productId) values(2, 12);
# INSERT INTO bag(userId, productId) values(2, 13);
