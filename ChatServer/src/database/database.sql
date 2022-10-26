CREATE DATABASE ChatMe;
USE ChatMe;

CREATE TABLE Users 
(
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    pass varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO Users
(
    name, pass
) 
VALUES 
(
    'Mario', '123'
),
(
    'Isaac', '123'
), 
(
    'Alexis', '123'
),
(
    'Javier', '123'
), 
(
    'Sara', '123'
),
(
    'Juan', '123'
);
