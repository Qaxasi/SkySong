 INSERT INTO users (username, email, password)
     VALUES('User', 'mail@mail.com',
       '$2a$10$WeBMjqOaJviwm/DV1BRvdOl19JrCMsJyrPUCFDaP.FnxJ.sqwSFiW');

INSERT INTO users (username, email, password)
   VALUES('existUsername', 'existMail@mail.com',
     '$2a$10$WeBMjqOaJviwm/DV1BRvdOl19JrCMsJyrPUCFDaP.FnxJ.sqwSFiW');

INSERT INTO users (username, email, password)
    VALUES('testAdmin', 'testAdmin@gmail.com',
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