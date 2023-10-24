ALTER TABLE roles AUTO_INCREMENT = 1;

INSERT INTO roles (name)
    VALUES('ROLE_USER'),
          ('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES(1, 1);