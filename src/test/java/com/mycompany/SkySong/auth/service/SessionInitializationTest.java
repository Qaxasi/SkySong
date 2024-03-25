package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.auth.security.UserSessionChecker;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import static com.mycompany.SkySong.testsupport.assertions.ExceptionAssertionUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;

public class SessionCreationServiceTest extends BaseIT {

    @Autowired
    private SessionCreationService login;
    @Autowired
    private UserSessionChecker sessionChecker;
    @Autowired
    private LoginRequests loginHelper;
    
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
    void whenLoginFailure_SessionNotCreated() {
        assertThrows(BadCredentialsException.class, () -> login.initializeSession(loginHelper.loginInvalidPassword("User")));
        assertFalse(sessionChecker.userHasSession("User"));
    }

    @Test
    void whenInvalidPassword_ThrowException() {
        assertException(() -> login.initializeSession(loginHelper.invalidPassword), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }

    @Test
    void whenInvalidEmail_ThrowException() {
        assertException(() -> login.initializeSession(loginHelper.invalidEmail), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }

    @Test
    void whenInvalidUsername_ThrowException() {
        assertException(() -> login.initializeSession(loginHelper.invalidUsername), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }

    @Test
    void whenLoginSuccess_CreateSession() {
        login.initializeSession(loginHelper.login("User"));
        assertTrue(sessionChecker.userHasSession("User"));
    }

    @Test
    void whenLoginSuccess_ReturnSessionId() {
        String sessionId = login.initializeSession(loginHelper.validCredentials);
        assertNotNull(sessionId);
    }
}
