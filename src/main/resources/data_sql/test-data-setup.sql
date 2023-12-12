INSERT INTO users (username, email, password)
    VALUES('existUsername', 'existMail@mail.com',
      '$2a$10$VEbWwz6NcL4y6MgKEE/sJuWiFe2EoVbru6gJ.6Miu6G16NWfqlxci');

INSERT INTO users (username, email, password)
    VALUES('testAdmin', 'testAdmin@gmail.com',
     '$2a$10$VEbWwz6NcL4y6MgKEE/sJuWiFe2EoVbru6gJ.6Miu6G16NWfqlxci');

INSERT INTO roles (name)
    VALUES('ROLE_USER'),
          ('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'testExistUsername' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'testAdmin' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'testAdmin' AND r.name = 'ROLE_ADMIN';