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
('ppbK2qEIwHXFK-3aSN0BzxVMPgMqfF7q4Tt1HRLiV1k=', (SELECT id FROM users WHERE username = 'User')
,NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR)),

('3at2PzvRMMxevkqoXBPhKZMgq5_71cjuK2W-fZLesV0=', (SELECT id FROM users WHERE username = 'Mark'),
NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR)),

('JCi2k1kVZ6ppsdOFNzPVVqPQ9KUj7HYPoS4Wj7Rze74=', (SELECT id FROM users WHERE username = 'testAdmin'),
 NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR));
