 CREATE TABLE users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(50) NOT NULL UNIQUE,
     email VARCHAR(100) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     enabled TINYINT(1) NOT NULL DEFAULT 1,
     locked TINYINT(1) NOT NULL DEFAULT 0,
     user_key BINARY(16) NOT NULL
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

 CREATE TABLE user_roles (
     user_id INT NOT NULL,
     role_id INT NOT NULL,
     PRIMARY KEY (user_id, role_id),
     KEY idx_user_roles_role_id (role_id),
     CONSTRAINT fk_user_roles_user
       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
     CONSTRAINT fk_user_roles_role
       FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT
 );
