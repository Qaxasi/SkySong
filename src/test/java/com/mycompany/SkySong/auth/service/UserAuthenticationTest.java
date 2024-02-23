package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;


public class UserAuthenticationTest extends BaseIT {
    @Autowired
    private UserAuthentication auth;
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
    
    @Test
    void whenValidCredentials_AuthenticationNotNull() {
        Authentication result = auth.authenticateUser(LoginRequests.VALID_CREDENTIALS);
        assertNotNull(result);
    }

    @Test
    void whenValidCredentials_UserAuthenticated() {
        Authentication result = auth.authenticateUser(LoginRequests.VALID_CREDENTIALS);
        assertTrue(result.isAuthenticated());
    }

    @Test
    void whenInvalidCredentials_AuthenticationFails() {
        assertThrows(BadCredentialsException.class,
                () -> auth.authenticateUser(LoginRequests.INVALID_PASSWORD));
    }
}
