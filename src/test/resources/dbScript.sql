DROP DATABASE IF EXISTS store;
CREATE DATABASE store;
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
    propertyValueUk VARCHAR(30),
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


INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Telephone', 'Телефон', 1000, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('TV', 'ТВ', 1500, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Car', 'Автомобіль', 13000, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Washer', 'Пральна машина', 800, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Blanked', 'Ковдра', 100, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Computer', 'Комп\'ютер', 1200, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Schoolbag', 'Рюкзак', 120, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Sofa', 'Диван', 500, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Vacuum', 'Пылесос', 300, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Fridge', 'Холодильник', 600, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Iron', 'Праска', 150, NOW());
INSERT INTO product(nameEn, nameUk, price, creationDate) values ('Kettle', 'Чайник', 50, NOW());


INSERT INTO property(productId, propertyNameEn, propertyValueEn, propertyNameUk, propertyValueUk)
    values (1, 'Model', 'pixel4', 'Модель','pixel4');

INSERT INTO property(productId, propertyNameEn, propertyValueEn, propertyNameUk, propertyValueUk)
values (1, 'Color', 'grey', 'Колір','сірий');


INSERT INTO property(productId, propertyNameEn, propertyValueEn, propertyNameUk, propertyValueUk)
values (2, 'Model', 'LG', 'Модель','LG');

INSERT INTO property(productId, propertyNameEn, propertyValueEn, propertyNameUk, propertyValueUk)
values (2, 'Size', 'big', 'Розмір','великий');

INSERT INTO property(productId, propertyNameEn, propertyValueEn, propertyNameUk, propertyValueUk)
values (3, 'Model', 'audi a6', 'Модель','audi a6');

INSERT INTO property(productId, propertyNameEn, propertyValueEn, propertyNameUk, propertyValueUk)
values (3, 'Size', 'very big', 'Розмір', 'дуже великий');


INSERT INTO property(productId, propertyNameEn, propertyValueEn, propertyNameUk, propertyValueUk)
values (3, 'Color', 'green', 'Колір', 'зелений');


INSERT INTO user(login, password, nameEn, nameUk, role) values ('qwerty@google.com', '12345', 'First User', 'Перший Користувач','USER');
INSERT INTO user(login, password, nameEn, nameUk, role) values ('artur.sarahman@gmail.com', 'qwerty', 'Artur Sarakhman', 'Артур Сарахман','ADMIN');

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
