package com.mycompany.SkySong.adapter.login.service;

import com.mycompany.SkySong.adapter.login.dto.LoginRequest;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.Assert.assertThrows;

class LoginHandlerTest extends BaseIT {

    @Autowired
    private LoginHandler login;
    @Autowired
    private UserFixture userFixture;
    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    private void createUser(String username, String password) {
        userFixture.createUserWithUsernameAndPassword(username, password);
    }

    private void login(String usernameOrEmail, String password) {
        login.login(usernameOrEmail, password);
    }
}
