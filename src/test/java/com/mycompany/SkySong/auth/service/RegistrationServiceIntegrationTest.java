package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.testsupport.TestMessages;
import com.mycompany.SkySong.testsupport.auth.service.DatabaseHelper;
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
    private RegistrationService registration;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DatabaseHelper databaseHelper;
    @Autowired
    private LoginService login;
    @BeforeEach
    void init() throws Exception {
        databaseHelper.setup("data_sql/test-data-setup.sql");
    }
    @AfterEach
    void cleanup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
//    @Test
//    void shouldRegisterUser() throws DatabaseException, SQLException {
//        RegisterRequest request = RegistrationHelper.createValidRegisterRequestWithUsername("testUsername");
//        registrationService.register(request);
//        assertTrue(databaseHelper.doesUserExist(request.username()));
//    }
//    @Test
//    void shouldAllowLoginForRegisterUser() throws DatabaseException {
//        RegistrationHelper.executeValidUserRegistration(registrationService);
//        assertNotNull(loginService.login(RegistrationHelper.createUserLoginRequest()));
//    }
//
//    @Test
//    void shouldAssignRoleUserToNewUser() throws DatabaseException, SQLException {
//        registrationService.register(RegistrationHelper.createValidRegisterRequestWithUsername("testUsername"));
//        assertTrue(databaseHelper.hasUserRole("testUsername", UserRole.ROLE_USER.name()));
//    }
//    @Test
//    void shouldReturnSuccessMessageOnUserRegistration () throws DatabaseException {
//        ApiResponse response = registrationService.register(RegistrationHelper.createValidRegisterRequest());
//        assertEquals(TestMessages.SUCCESS_REGISTRATION, response.message());
//    }
    @Test
    void shouldThrowExceptionForInvalidUsernameFormat() {
        assertException(() -> registration.register(RegistrationHelper.invalidUsername),
                CredentialValidationException.class, "Invalid username format. The username can contain only letters" +
                        " and numbers, and should be between 3 and 20 characters long.");
    }
    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        assertException(() -> registration.register(RegistrationHelper.invalidEmail),
                CredentialValidationException.class, "Invalid email address format. The email should follow the " +
                        "standard format (e.g., user@example.com) and be between 6 and 30 characters long.");
    }
    @Test
    void shouldThrowExceptionForInvalidPasswordFormat() {
        assertException(() -> registration.register(RegistrationHelper.invalidPassword),
                CredentialValidationException.class, "Invalid password format. The password must contain an least 8 " +
                        "characters, including uppercase letters, lowercase letters, numbers, and special characters.");
    }
    @Test
    void shouldThrowExceptionForExistUsername() {
        assertException(() -> registration.register(RegistrationHelper.existingUsername),
                CredentialValidationException.class, "Username is already exist!.");
    }
    @Test
    void shouldThrowExceptionForExistEmail() {
        assertException(() -> registration.register(RegistrationHelper.existEmail),
                CredentialValidationException.class, "Email is already exist!.");
    }
}
