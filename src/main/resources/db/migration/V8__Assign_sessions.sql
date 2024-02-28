INSERT INTO sessions (session_id, user_id, create_at, expires_at)
VALUES
('xAUpqIbS2L9_ULU39L7ZP07RJufNgFizawVK68qTyrw=', (SELECT id FROM users WHERE username = 'Admin')
,NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR));