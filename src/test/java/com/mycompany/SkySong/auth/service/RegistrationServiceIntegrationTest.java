package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.service.RegistrationServiceImpl;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.repository.UserDAO;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegistrationServiceIntegrationTest {
    @Autowired
    private RegistrationServiceImpl registrationService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;
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
    void shouldRegisterUserWithCorrectAttributesAndSaveToDatabase() throws DatabaseException {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        registrationService.register(registerRequest);

        Optional<User> registeredUser = userDAO.findByUsername(registerRequest.username());

        assertTrue(registeredUser.isPresent());
        assertEquals(registerRequest.username(), registeredUser.get().getUsername());
        assertEquals(registerRequest.email(), registeredUser.get().getEmail());
    }
    @Test
    void shouldThrowExceptionForInvalidUsernameFormatOnRegistration() throws DatabaseException {
        RegisterRequest registerRequest = new RegisterRequest(
                "invalidUsername$Format", "testUniqueEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowExceptionForInvalidEmailFormatOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "invalidEmailFormat", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowExceptionForInvalidPasswordFormatOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "invalidFormat");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenTryRegisterWithExistingUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenTryRegisterWithExistingEmail() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldIncrementUserCountByOneWhenUserSuccessfullyRegistered() throws DatabaseException {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        long userCountBefore = userDAO.count();

        registrationService.register(registerRequest);

        long userCountAfter = userDAO.count();

        assertThat(userCountAfter).isEqualTo(userCountBefore + 1);
    }
    @Test
    void shouldNotIncrementUserCountWhenRegistrationFails() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        long userCountBefore = userDAO.count();

        try {
            registrationService.register(registerRequest);
        } catch (RegisterException | DatabaseException ignored) {
        }

        long userCountAfter = userDAO.count();

        assertEquals(userCountBefore, userCountAfter);
    }
    @Test
    void shouldNotIncrementUserCountWhenEmailRegistrationFails() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testEmail@gmail.com", "testPassword@123");

        long userCountBefore = userDAO.count();

        try {
            registrationService.register(registerRequest);
        } catch (RegisterException | DatabaseException ignored) {
        }

        long userCountAfter = userDAO.count();

        assertEquals(userCountBefore, userCountAfter);
    }
    @Test
    void shouldCheckPasswordHashingOnRegistration() throws DatabaseException {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        ApiResponse response = registrationService.register(registerRequest);
        assertEquals("User registered successfully", response.message());

        Optional<User> user = userDAO.findByUsername(registerRequest.username());
        assertTrue(user.isPresent());
        String encodedPasswordInDB = user.get().getPassword();
        assertNotEquals(registerRequest.password(), encodedPasswordInDB);
        assertTrue(passwordEncoder.matches(registerRequest.password(), encodedPasswordInDB));
    }
}
