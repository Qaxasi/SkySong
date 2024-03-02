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
import org.springframework.security.core.context.SecurityContextHolder;

import static com.mycompany.SkySong.testsupport.assertions.ExceptionAssertionUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest extends BaseIT {

    @Autowired
    private LoginService login;
    @Autowired
    private UserSessionChecker sessionChecker;
    private LoginRequests loginHelper;
    
    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        loginHelper = new LoginRequests();
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenLoginFailure_SessionNotCreated() {
        assertThrows(BadCredentialsException.class, () -> login.login(loginHelper.loginInvalidPassword("User")));
        assertFalse(sessionChecker.userHasSession("User"));
    }

    @Test
    void whenInvalidPassword_ThrowException() {
        assertException(() -> login.login(loginHelper.invalidPassword), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }

    @Test
    void whenInvalidEmail_ThrowException() {
        assertException(() -> login.login(loginHelper.invalidEmail), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }

    @Test
    void whenInvalidUsername_ThrowException() {
        assertException(() -> login.login(loginHelper.invalidUsername), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }

    @Test
    void whenLoginFailure_NotSetAuthContext() {
        assertThrows(BadCredentialsException.class, () -> login.login(loginHelper.invalidPassword));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void whenLoginSuccess_CreateSession() {
        login.login(loginHelper.login("User"));
        assertTrue(sessionChecker.userHasSession("User"));
    }

    @Test
    void whenLoginSuccess_ReturnSessionId() {
        String sessionId = login.login(loginHelper.validCredentials);
        assertNotNull(sessionId);
    }

    @Test
    void whenLoginSuccess_SetAuthContext() {
        login.login(loginHelper.validCredentials);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
