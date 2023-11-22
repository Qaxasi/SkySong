package com.mycompany.SkySong.auth.service;

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
    void shouldRegisterUserWithCorrectAttributesAndSaveToDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        registrationService.register(registerRequest);

        Optional<User> registeredUser = userDAO.findByUsername(registerRequest.username());

        assertTrue(registeredUser.isPresent());
        assertEquals(registerRequest.username(), registeredUser.get().getUsername());
        assertEquals(registerRequest.email(), registeredUser.get().getEmail());
    }
    @Test
    void shouldCheckExistenceOfRegisteredUserInDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        registrationService.register(registerRequest);

        assertTrue(userDAO.existsByUsername(registerRequest.username()));
    }
    @Test
    void shouldIncrementUserCountByOneWhenUserSuccessfullyRegistered() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        long userCountBefore = userDAO.count();

        registrationService.register(registerRequest);

        long userCountAfter = userDAO.count();

        assertThat(userCountAfter).isEqualTo(userCountBefore + 1);
    }
    @Test
    void shouldThrowExceptionWhenUserTryRegisterWithExistUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldNotIncrementUserCountWhenUsernameRegistrationFails() {
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

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldNotIncrementUserCountWhenEmailRegistrationFails() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testEmail@gmail.com", "testPassword@123");

        long userCountBefore = userDAO.count();

        try {
            registrationService.register(registerRequest);
        } catch (RegisterException ignored) {
        }

        long userCountAfter = userDAO.count();

        assertEquals(userCountBefore, userCountAfter);
    }
    @Test
    void shouldCheckPasswordHashingOnRegistration() {
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
