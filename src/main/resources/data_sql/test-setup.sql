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
WHERE u.username = 'Mark' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'testAdmin' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'testAdmin' AND r.name = 'ROLE_ADMIN';

INSERT INTO sessions (session_id, user_id, create_at, expires_at)
VALUES
('yDCNcRp0UsS0TS68bdzXwti1QToDCPivrJ-Z-1scQ3w=', (SELECT id FROM users WHERE username = 'Mark'),
NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR)),

('tRcutVkNlaMucrbedlekAW9n-jRr50hS8emfUYyq01c=', (SELECT id FROM users WHERE username = 'testAdmin'),
 NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR));
