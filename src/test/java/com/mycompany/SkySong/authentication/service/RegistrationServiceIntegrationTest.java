package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.repository.RoleDAO;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.impl.RegistrationServiceImpl;
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

import javax.management.relation.RoleResult;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

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
    void shouldRegisterUserAndSaveToDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        registrationService.register(registerRequest);

        Optional<User> registeredUser = userDAO.findByUsername(registerRequest.username());

        assertTrue(registeredUser.isPresent());
        assertEquals(registerRequest.username(), registeredUser.get().getUsername());
        assertEquals(registerRequest.email(), registeredUser.get().getEmail());
    }
    @Test
    void shouldThrowExceptionWhenUserTryRegisterWithExistUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldNotAddNewUserWhenRegistrationFailed() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        long userCountBefore = userDAO.count();

        try {
            registrationService.register(registerRequest);
        } catch (RegisterException ignored) {
        }

        long userCountAfter = userDAO.count();

        assertEquals(userCountBefore, userCountAfter);
    }
    @Test
    void shouldThrowExceptionWhenUserTryRegisterWithExistEmail() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testEmail@gmail.com", "testPassword@123");

        long userCountBefore = userDAO.count();

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        long userCountAfter = userDAO.count();

        assertEquals(userCountBefore, userCountAfter);
    }
    @Test
    void shouldThrowExceptionWhenUserTryRegisterWithInvalidPasswordFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUser", "testUniqueEmail@gmail.com", "invalidPassword");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        assertFalse(userDAO.existsByUsername(registerRequest.username()));
    }

    @Test
    void shouldThrowExceptionWhenUserTryRegisterWithInvalidEmailFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "invalidEmail", "testPassword@123");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        assertFalse(userDAO.existsByUsername(registerRequest.email()));
    }
    @Test
    void shouldThrowExceptionWhenUserTryRegisterWithInvalidUsernameFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "invalid%username", "testUniqueEmail@gmail.com", "testPassword@123");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        assertFalse(userDAO.existsByUsername(registerRequest.username()));
    }
    @Test
    void shouldCheckPasswordHashingOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        RegistrationResponse response = registrationService.register(registerRequest);
        assertEquals("User registered successfully", response.message());

        Optional<User> user = userDAO.findByUsername(registerRequest.username());
        assertTrue(user.isPresent());
        String encodedPasswordInDB = user.get().getPassword();
        assertNotEquals(registerRequest.password(), encodedPasswordInDB);
        assertTrue(passwordEncoder.matches(registerRequest.password(), encodedPasswordInDB));
    }

}
