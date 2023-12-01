package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import io.jsonwebtoken.Claims;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginServiceImplIntegrationTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AuthenticationManager authenticationManager;
    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/test-data-setup.sql"));
        }
    }
    @AfterEach
    void cleanup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
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
    void shouldNotAuthenticateWhenLoginWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest(
                "testWrongUsername", "testWrongPassword@123");

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldAuthenticateWhenLoginWithValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        loginService.login(loginRequest);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldReturnCorrectUserNameInAuthenticationWhenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        loginService.login(loginRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(loginRequest.usernameOrEmail(), authentication.getName());
    }
}
