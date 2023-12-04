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
    void shouldAssignRoleUserToNewUser() throws DatabaseException {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUniqueUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        registrationService.register(registerRequest);

        Optional<User> userOptional = userDAO.findByUsername("testUniqueUsername");
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();

        Optional<Role> roleOptional = roleDAO.findByName(UserRole.ROLE_USER);
        assertTrue(roleOptional.isPresent());

        Role userRole = roleOptional.get();

        assertTrue(user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(userRole.getName())));
    }
}
