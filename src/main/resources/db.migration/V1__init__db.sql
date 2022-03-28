CREATE TABLE products(
   id SERIAL PRIMARY KEY,
   name VARCHAR(50),
   price VARCHAR(50),
   creation_date VARCHAR(50)
);

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    email VARCHAR(50),
    password VARCHAR(50),
    gender VARCHAR(50),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    about TEXT,
    age INT NOT NULL DEFAULT 0
);