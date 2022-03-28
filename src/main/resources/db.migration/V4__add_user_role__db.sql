CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');
ALTER TABLE users
ADD COLUMN role user_role;