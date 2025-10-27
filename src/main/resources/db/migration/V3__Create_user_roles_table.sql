CREATE TABLE IF NOT EXISTS user_roles (
   user_id INT UNSIGNED NOT NULL,
   role_code ENUM('USER', 'ADMIN') NOT NULL,
   PRIMARY KEY (user_id, role_code),
   CONSTRAINT fk_user_roles_user
       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);