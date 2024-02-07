CREATE TABLE sessions (
    session_id VARCHAR(255) PRIMARY KEY,
    user_id INTEGER NOT NULL,
    create_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL
);