INSERT INTO user_roles (user_id, role_id) VALUES
((SELECT id FROM users WHERE username = 'Admin'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));

INSERT INTO user_roles (user_id, role_id) VALUES
((SELECT id FROM users WHERE username = 'Admin'), (SELECT id FROM roles WHERE name = 'ROLE_USER'));