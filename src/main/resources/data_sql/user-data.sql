ALTER TABLE users AUTO_INCREMENT = 1;

INSERT INTO users (username, email, password)
    VALUES('testUsername', 'testEmail@gmail.com',
     '$2a$10$VEbWwz6NcL4y6MgKEE/sJuWiFe2EoVbru6gJ.6Miu6G16NWfqlxci');

INSERT INTO users (username, email, password)
    VALUES('testAdmin', 'testAdmin@gmail.com',
     '$2a$10$VEbWwz6NcL4y6MgKEE/sJuWiFe2EoVbru6gJ.6Miu6G16NWfqlxci');