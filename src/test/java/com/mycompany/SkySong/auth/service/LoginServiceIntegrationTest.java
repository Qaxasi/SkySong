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

import static com.mycompany.SkySong.testsupport.auth.service.LoginServiceIntegrationTestHelper.*;
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
    void whenLoginSuccess_ReturnValidToken() {
        String token = login.login(validRequest);
        assertTrue(tokenProvider.validateToken(token));
    }
    @Test
    void whenInvalidPassword_ThrowException() {
        assertThrows(BadCredentialsException.class, () -> login.login(invalidPassword));
    }
    @Test
    void whenInvalidEmail_ThrowException() {
        assertThrows(BadCredentialsException.class, () -> login.login(invalidEmail));
    }
    @Test
    void whenInvalidUsername_ThrowException() {
        assertThrows(BadCredentialsException.class, () -> login.login(invalidUsername));
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
    void whenValidCre_ReturnCorrectUsernameInAuth() {
        assertUserAuthWithUsername("User", login);
    }
//    @Test
//    void shouldReturnErrorMessageAfterLoginWithInvalidCredentials() {
//        LoginRequest loginRequest = new LoginRequest(
//                "testWrongUsername", "testWrongPassword@123");
//
//        String expectedMessage = "Incorrect username/email or password.";
//
//        Exception exception = assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
//
//        assertEquals(expectedMessage, exception.getMessage());
//    }
}
