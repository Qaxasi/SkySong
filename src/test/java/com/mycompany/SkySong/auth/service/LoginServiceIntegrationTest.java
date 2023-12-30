package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.testsupport.common.DatabaseHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginServiceIntegrationTest {
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
    void shouldReturnValidTokenAfterSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        String token = loginService.login(loginRequest);

        assertTrue(jwtTokenProvider.validateToken(token));
    }
    @Test
    void shouldThrowExceptionWhenEmailLoggingWithInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("testEmail@gmail.com", "invalidPassword@123");

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }
    @Test
    void shouldThrowExceptionWhenUsernameLoginWithInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "invalidPassword@123");

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }
    @Test
    void shouldThrowExceptionWhenInvalidEmailLogin() {
        LoginRequest loginRequest = new LoginRequest("invalidEmail@gmail.com", "testPassword@123");

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }
    @Test
    void shouldThrowExceptionWhenInvalidUsernameLogin() {
        LoginRequest loginRequest = new LoginRequest("invalidUsername", "testPassword@123");

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }
    @Test
    void shouldNotSetAuthContextWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest(
                "testWrongUsername", "testWrongPassword@123");

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldSetAuthContextWhenLoginWithValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        loginService.login(loginRequest);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldReturnCorrectUsernameInAuthenticationWhenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        loginService.login(loginRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(loginRequest.usernameOrEmail(), authentication.getName());
    }
    @Test
    void shouldReturnErrorMessageAfterLoginWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest(
                "testWrongUsername", "testWrongPassword@123");

        String expectedMessage = "Incorrect username/email or password.";

        Exception exception = assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
