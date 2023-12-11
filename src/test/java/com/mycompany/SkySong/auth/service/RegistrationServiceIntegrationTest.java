package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.UserRole;
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
    private RegistrationService registrationService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DatabaseHelper databaseHelper;
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
        RegisterRequest request = RegistrationHelper.createValidRegisterRequestWithUsername("testUsername");
        registrationService.register(request);
        assertTrue(databaseHelper.doesUserExist(request.username()));
    }
    @Test
    void shouldAllowLoginForRegisterUser() throws DatabaseException {
        RegistrationHelper.executeValidUserRegistration(registrationService);
        assertNotNull(loginService.login(RegistrationHelper.createUserLoginRequest()));
    }

    @Test
    void shouldAssignRoleUserToNewUser() throws DatabaseException, SQLException {
        registrationService.register(RegistrationHelper.createValidRegisterRequestWithUsername("testUsername"));
        assertTrue(databaseHelper.hasUserRole("testUsername", UserRole.ROLE_USER.name()));
    }
    @Test
    void shouldReturnSuccessMessageOnUserRegistration () throws DatabaseException {
        ApiResponse response = registrationService.register(RegistrationHelper.createValidRegisterRequest());
        assertEquals(TestMessages.SUCCESS_REGISTRATION, response.message());
    }
    @Test
    void shouldThrowExceptionForInvalidUsernameFormat() {
        assertValidationException(() -> registrationService.register(
                RegistrationHelper.createInvalidUsernameRequest()));
    }
    @Test
    void shouldReturnErrorMessageForInvalidUsernameFormat() {
       assertErrorMessage(() -> registrationService.register(
               RegistrationHelper.createInvalidUsernameRequest()), TestMessages.INVALID_USERNAME_FORMAT);
    }
    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        assertValidationException(() -> registrationService.register(
                RegistrationHelper.createInvalidEmailRequest()));
    }
    @Test
    void shouldReturnErrorMessageForInvalidEmailFormat() {
        assertErrorMessage(() -> registrationService.register(
                RegistrationHelper.createInvalidEmailRequest()), TestMessages.INVALID_EMAIL_FORMAT);
    }
    @Test
    void shouldThrowExceptionForInvalidPasswordFormat() {
        assertValidationException(() -> registrationService.register(
                RegistrationHelper.createInvalidPasswordRequest()));
    }
    @Test
    void shouldReturnErrorMessageForInvalidPasswordFormat() {
        assertErrorMessage(() -> registrationService.register(
                RegistrationHelper.createInvalidPasswordRequest()), TestMessages.INVALID_PASSWORD_FORMAT);
    }
    @Test
    void shouldThrowExceptionForExistUsername() {
        assertValidationException(() -> registrationService.register(
                RegistrationHelper.createExistUsernameRequest()));
    }
    @Test
    void shouldReturnErrorMessageForExistUsername() {
        assertErrorMessage(() -> registrationService.register(
                RegistrationHelper.createExistUsernameRequest()), TestMessages.USERNAME_EXIST);
    }
    @Test
    void shouldThrowExceptionForExistEmail() {
        assertValidationException(() -> registrationService.register(
                RegistrationHelper.createExistEmailRequest()));
    }
    @Test
    void shouldReturnErrorMessageForExistEmail() {
        assertErrorMessage(() -> registrationService.register(
                RegistrationHelper.createExistEmailRequest()), TestMessages.EMAIL_EXIST);
    }
}
