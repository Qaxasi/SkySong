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
import org.springframework.security.core.context.SecurityContextHolder;

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
    void whenAuthenticationSuccess_AuthenticationNotNull() {
        Authentication result = auth.authenticateUser(LoginRequests.VALID_CREDENTIALS);
        assertNotNull(result);
    }

    @Test
    void whenAuthenticationSuccess_UserAuthenticated() {
        Authentication result = auth.authenticateUser(LoginRequests.VALID_CREDENTIALS);
        assertTrue(result.isAuthenticated());
    }

    @Test
    void whenAuthenticationFails_ThrowException() {
        assertThrows(BadCredentialsException.class,
                () -> auth.authenticateUser(LoginRequests.INVALID_PASSWORD));
    }

    @Test
    void whenAuthenticationSuccess_SetSecurityContext() {
       auth.authenticateUser(LoginRequests.VALID_CREDENTIALS);
       assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void whenAuthenticationFails_SecurityContextNotSet() {
        assertThrows(BadCredentialsException.class,
                () -> auth.authenticateUser(LoginRequests.INVALID_PASSWORD));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
