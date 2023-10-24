package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.LoginRequest;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.BadCredentialsException;
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
    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/user-data.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/role-data.sql"));

        }
    }
    @AfterEach
    void cleanup() throws Exception {
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
    void shouldReturnErrorAndCorrectErrorMessageAfterLoginWithWrongUsername() {
        LoginRequest loginRequest = new LoginRequest("testWrongUsername", "testPassword@123");

        Exception exception = assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
        String expectedMessage = "Incorrect username/email or password";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldReturnErrorAndCorrectErrorMessageAfterLoginWithWrongEmail() {
        LoginRequest loginRequest = new LoginRequest(
                "testWrongEmail@gmail.com", "testPassword@123");

        Exception exception = assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
        String expectedMessage = "Incorrect username/email or password";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldReturnErrorAndCorrectErrorMessageAfterLoginWithWrongPasswordWhenUsernameIsCorrect() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testWrongPassword@123");

        Exception exception = assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
        String expectedMessage = "Incorrect username/email or password";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldReturnErrorAndCorrectErrorMessageAfterLoginWithWrongPasswordWhenEmailIsCorrect() {
        LoginRequest loginRequest = new LoginRequest(
                "testEmail@gmail.com", "testWrongPassword@123");

        Exception exception = assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
        String expectedMessage = "Incorrect username/email or password";

        assertEquals(expectedMessage, exception.getMessage());

    }
    @Test
    void shouldContainCorrectUserInToken() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        String token = loginService.login(loginRequest);

        String username = jwtTokenProvider.getSubjectFromToken(token);

        assertEquals(loginRequest.usernameOrEmail(), username);
    }
}
