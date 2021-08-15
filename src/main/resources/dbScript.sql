CREATE DATABASE IF NOT EXISTS store;
use store;

CREATE TABLE IF NOT EXISTS product
(
    id INT AUTO_INCREMENT,
    nameEn VARCHAR(100),
    nameUk VARCHAR(100),
    price DOUBLE,
    creationDate DATETIME,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS property
(
    productId INT,
    propertyNameEn VARCHAR(30),
    propertyValueEn VARCHAR(30),
    propertyNameUk VARCHAR(30),
    propertyValueUr VARCHAR(30),
    CONSTRAINT propertyFK
    FOREIGN KEY (productId) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user
(
    id INT AUTO_INCREMENT,
    login VARCHAR(50) NULL,
    password VARCHAR(50) NULL,
    nameEn VARCHAR(50) NULL,
    nameUk VARCHAR(50) NULL,
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


INSERT INTO product(nameEn, price, creationDate) values ('Telephone', 1000, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('TV', 1500, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('CAR', 13000, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod1', 1001, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod2', 1002, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod3', 1003, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod4', 1004, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod5', 1005, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod6', 1006, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod7', 1007, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod8', 1008, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod9', 1009, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod10', 1010, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod11', 1011, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod12', 1012, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod13', 1013, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod14', 1014, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod15', 1015, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod16', 1016, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod17', 1017, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod18', 1018, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod19', 1019, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod20', 1020, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod21', 1021, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod22', 1022, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod23', 1023, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod24', 1024, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod25', 1025, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod26', 1026, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod27', 1027, NOW());
INSERT INTO product(nameEn, price, creationDate) values ('prod28', 1028, NOW());

INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (1, 'Model', 'iPhone X');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (1, 'Color', 'white');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (3, 'Model', 'Subaru Forester');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (5, 'prop1', 'value1');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (6, 'prop2', 'value2');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (7, 'prop3', 'value3');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (8, 'prop4', 'value4');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (9, 'prop5', 'value5');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (10, 'prop6', 'value6');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (11, 'prop7', 'value7');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (12, 'prop8', 'value8');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (13, 'prop9', 'value9');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (14, 'prop10', 'value10');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (15, 'prop11', 'value11');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (16, 'prop12', 'value12');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (17, 'prop13', 'value13');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (18, 'prop14', 'value14');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (19, 'prop15', 'value15');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (20, 'prop16', 'value16');
INSERT INTO property(productId, propertyNameEn, propertyValueEn) values (21, 'prop17', 'value17');

INSERT INTO user(login, password, nameEn, role) values ('qwer', '12345', 'first user', 'USER');
INSERT INTO user(login, password, nameEn, role) values ('Artur', 'qwerty', 'Artur Sarakhman', 'ADMIN');

INSERT INTO bag(userId, productId) values(1, 1);
INSERT INTO bag(userId, productId) values(1, 2);
INSERT INTO bag(userId, productId) values(1, 3);
INSERT INTO bag(userId, productId) values(1, 4);
INSERT INTO bag(userId, productId) values(1, 5);
INSERT INTO bag(userId, productId) values(1, 6);
INSERT INTO bag(userId, productId) values(1, 7);
INSERT INTO bag(userId, productId) values(2, 8);
INSERT INTO bag(userId, productId) values(2, 9);
INSERT INTO bag(userId, productId) values(2, 10);
INSERT INTO bag(userId, productId) values(2, 11);
INSERT INTO bag(userId, productId) values(2, 12);
INSERT INTO bag(userId, productId) values(2, 13);
