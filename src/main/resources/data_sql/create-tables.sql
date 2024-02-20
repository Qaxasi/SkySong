 CREATE TABLE users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(50) NOT NULL UNIQUE,
     email VARCHAR(100) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
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

 CREATE TABLE sessions (
     session_id VARCHAR(255) PRIMARY KEY,
     user_id INTEGER NOT NULL,
     create_at TIMESTAMP NOT NULL,
     expires_at TIMESTAMP NOT NULL
 );