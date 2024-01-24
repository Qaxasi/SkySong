 CREATE TABLE users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(50) NOT NULL UNIQUE,
     email VARCHAR(100) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     enabled BOOLEAN NOT NULL DEFAULT TRUE
     );

 CREATE TABLE roles (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(50) NOT NULL UNIQUE
 );

 CREATE TABLE user_roles (
     user_id INT NOT NULL,
     role_id INT NOT NULL,
     PRIMARY KEY (user_id, role_id),
     FOREIGN KEY (user_id) REFERENCES users (id),
     FOREIGN KEY (role_id) REFERENCES roles (id)
 );

INSERT INTO users (username, email, password)
    VALUES('User', 'mail@mail.com',
        '$2a$10$WeBMjqOaJviwm/DV1BRvdOl19JrCMsJyrPUCFDaP.FnxJ.sqwSFiW');

INSERT INTO users (username, email, password)
   VALUES('Mark', 'mark@mail.com',
      '$2a$10$WeBMjqOaJviwm/DV1BRvdOl19JrCMsJyrPUCFDaP.FnxJ.sqwSFiW');

INSERT INTO users (username, email, password)
    VALUES('testAdmin', 'testAdmin@mail.com',
       '$2a$10$WeBMjqOaJviwm/DV1BRvdOl19JrCMsJyrPUCFDaP.FnxJ.sqwSFiW');

INSERT INTO roles (name)
    VALUES('ROLE_USER'),
          ('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'User' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'existUser' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'testAdmin' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'testAdmin' AND r.name = 'ROLE_ADMIN';