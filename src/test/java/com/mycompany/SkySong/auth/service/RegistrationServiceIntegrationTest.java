package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.testsupport.auth.service.RegistrationHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.mycompany.SkySong.testsupport.auth.service.UserAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegistrationServiceIntegrationTest {
    @Autowired
    private RegistrationService registrationService;
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
    void shouldRegisterUser() throws DatabaseException, SQLException {
        RegisterRequest request = RegistrationHelper.createValidRegisterRequest();
        registrationService.register(request);
        assertUserExist(request.username());
    }
    @Test
    void shouldAllowLoginForRegisterUser() throws DatabaseException {
        RegistrationHelper.givenAndExistingUser();
        assertNotNull(loginService.login(RegistrationHelper.userLoginRequest()));
    }

    @Test
    void shouldAssignRoleUserToNewUser() throws DatabaseException, SQLException {
        registrationService.register(RegistrationHelper.createValidRegisterRequestWithUsername("testUsername"));
        assertUserRole("testUsername", UserRole.ROLE_USER.name());
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
        assertValidationException(() ->
                registrationService.register(RegistrationHelper.createInvalidUsernameRequest()));
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
        assertValidationException(() ->
                registrationService.register(RegistrationHelper.createInvalidEmailRequest()));
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
        assertValidationException(() ->
                registrationService.register(RegistrationHelper.createInvalidPasswordRequest()));
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
        assertValidationException(() -> registrationService.register(RegistrationHelper.createExistUsernameRequest()));
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
        assertValidationException(() -> registrationService.register(RegistrationHelper.createExistEmailRequest()));
    }
    @Test
    void shouldReturnErrorMessageWhenTryRegisterWithExistEmail() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testEmail@gmail.com", "testPassword@123");

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "Email is already exist!.";

        assertEquals(expectedMessage, exception.getMessage());
    }
}
