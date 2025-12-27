CREATE TABLE IF NOT EXISTS admin_delete_guard (
    id INT PRIMARY KEY
) ENGINE=InnoDB;

INSERT IGNORE INTO admin_delete_guard(id) VALUES (1);