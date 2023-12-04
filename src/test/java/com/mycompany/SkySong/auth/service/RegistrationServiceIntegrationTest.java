package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.service.RegistrationServiceImpl;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.testsupport.RegisterAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegistrationServiceIntegrationTest {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private LoginService loginService;
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
    void shouldRegisterUserAndAllowLoginWithValidCredentials() throws DatabaseException {
        String username = "testUniqueUsername";
        String email = "testUniqueEmail@gmail.com";
        String password = "testPassword@123";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        registrationService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(username, password);

        String token = loginService.login(loginRequest);
        assertNotNull(token, "JWT token has not been generated");
    }

    @Test
    void shouldAssignRoleUserToNewUser() throws DatabaseException, SQLException {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        registrationService.register(registerRequest);

        assertTrue(assertUserRoleAddedToNewUser(registerRequest.username(), UserRole.ROLE_USER.name()));
    }
    @Test
    void shouldReturnSuccessMessageOnUserRegistration () throws DatabaseException {
        String username = "testUniqueUsername";
        String email = "testUniqueEmail@gmail.com";
        String password = "testPassword@123";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        ApiResponse response = registrationService.register(registerRequest);

        assertEquals("User registered successfully.", response.message());
    }
    @Test
    void shouldThrowExceptionForInvalidUsernameFormatOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "invalidUsername$Format", "testUniqueEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldReturnErrorMessageForInvalidUsernameFormatOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "invalidUsername$Format", "testUniqueEmail@gmail.com", "testPassword@123");

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "Invalid username format. The username can contain only letters and numbers," +
                " and should be between 3 and 20 characters long.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionForInvalidEmailFormatOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "invalidEmailFormat", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldReturnErrorMessageForInvalidEmailFormatOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "invalidEmailFormat", "testPassword@123");

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "Invalid email address format. The email should follow the standard format" +
                " (e.g., user@example.com) and be between 6 and 30 characters long.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionForInvalidPasswordFormatOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "invalidFormat");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldReturnErrorMessageForInvalidPasswordFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "invalidFormat");

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "Invalid password format. The password must contain an least 8 characters, " +
                "including uppercase letters, lowercase letters, numbers, and special characters.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenTryRegisterWithExistingUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldReturnErrorMessageWhenTryRegisterWithExistingUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        Exception exception =assertThrows(CredentialValidationException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "Username is already exist!.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenTryRegisterWithExistingEmail() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    private boolean assertUserRoleAddedToNewUser(String username, String roleName) throws SQLException {
        String query = "SELECT COUNT(*) " +
                "FROM user_roles ur " +
                "JOIN users u ON ur.user_id = u.id " +
                "JOIN roles r ON ur.role_id = r.id " +
                "WHERE u.username = ? AND r.role_name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, roleName);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;
        }
    }
}
