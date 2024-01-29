package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.common.DatabaseHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;

import static com.mycompany.SkySong.testsupport.auth.service.LoginServiceIntegrationTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest extends BaseIT {
    @Autowired
    private LoginService login;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private DatabaseHelper databaseHelper;
    @BeforeEach
    void init() throws Exception {
        databaseHelper.setup("data_sql/test-data-setup.sql");
    }
    @AfterEach
    void cleanup() {
        databaseHelper.removeUsersAndRoles();
    }
    @Test
    void whenLoginSuccess_ReturnValidToken() {
        String token = login.login(validRequest);
        assertTrue(tokenProvider.validateToken(token));
    }
    @Test
    void whenInvalidPassword_ThrowException() {
        assertLoginFailureWithMessage(
                login, invalidPassword, BadCredentialsException.class, "Incorrect username/email or password.");
    }
    @Test
    void whenInvalidEmail_ThrowException() {
        assertLoginFailureWithMessage(
                login, invalidEmail, BadCredentialsException.class, "Incorrect username/email or password.");
    }
    @Test
    void whenInvalidUsername_ThrowException() {
        assertLoginFailureWithMessage(
                login, invalidUsername, BadCredentialsException.class, "Incorrect username/email or password.");
    }
    @Test
    void whenInvalidCredentials_NotSetAuthContext() {
        assertThrows(BadCredentialsException.class, () -> login.login(invalidUsername));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void whenValidCredentials_SetAuthContext() {
        login.login(validRequest);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void whenValidCredentials_ReturnCorrectUsernameInAuth() {
        assertUserAuthWithUsername("User", login);
    }
}
