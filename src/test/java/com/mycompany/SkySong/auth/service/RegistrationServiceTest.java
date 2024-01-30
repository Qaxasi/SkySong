package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.RegistrationHelper;
import com.mycompany.SkySong.testsupport.common.DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static com.mycompany.SkySong.ExceptionAssertionUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationServiceTest extends BaseIT {
    @Autowired
    private RegistrationService registration;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private LoginService login;
    @Autowired
    private DatabaseHelper databaseHelper;

    @Test
    @Transactional
    void whenValidCredentials_RegisterUser() {
        // given
        RegisterRequest request = RegistrationHelper.REGISTER("Alex");

        // when
        registration.register(request);

        // then
        assertTrue(databaseHelper.userExist("Alex"));
    }
    @Test
    @Transactional
    void whenRegistrationSuccess_AllowLoginForRegisterUser() {
        // given
        RegisterRequest request = RegistrationHelper.VALID_CREDENTIALS;

        // when
        registration.register(request);

        // then
        LoginRequest loginRequest = RegistrationHelper.LOGIN;
        assertNotNull(login.login(loginRequest));
    }
    @Test
    @Transactional
    void whenRegistrationSuccess_AssignRoleUserToNewUser() {
        // given
        RegisterRequest request = RegistrationHelper.REGISTER("Alex");

        // when
        registration.register(request);

        // then
        assertTrue(databaseHelper.hasUserRole("Alex", UserRole.ROLE_USER.name()));
    }
    @Test
    @Transactional
    void whenRegistrationSuccess_ReturnMessage () {
        // given
        RegisterRequest request = RegistrationHelper.VALID_CREDENTIALS;

        // when
        ApiResponse response = registration.register(request);

        // then
        assertEquals("User registered successfully." , response.message());
    }
    @Test
    void whenInvalidUsernameFormat_ThrowException() {
        // given
        RegisterRequest request = RegistrationHelper.INVALID_USERNAME;

        // when & then
        assertException(() -> registration.register(request),
                CredentialValidationException.class, "Invalid username format. The username can contain only letters" +
                        " and numbers, and should be between 3 and 20 characters long.");
    }
    @Test
    void whenInvalidEmailFormat_ThrowException() {
        // given
        RegisterRequest request = RegistrationHelper.INVALID_EMAIL;

        // when & then
        assertException(() -> registration.register(request),
                CredentialValidationException.class, "Invalid email address format. The email should follow the " +
                        "standard format (e.g., user@example.com) and be between 6 and 30 characters long.");
    }
    @Test
    void whenInvalidPasswordFormat_ThrowException() {
        // given
        RegisterRequest request = RegistrationHelper.INVALID_PASSWORD;

        // when & then
        assertException(() -> registration.register(request),
                CredentialValidationException.class, "Invalid password format. The password must contain an least 8 " +
                        "characters, including uppercase letters, lowercase letters, numbers, and special characters.");
    }
    @Test
    void whenUsernameExist_ThrowException() {
        // given
        RegisterRequest request = RegistrationHelper.EXIST_USERNAME;

        // when & then
        assertException(() -> registration.register(request),
                CredentialValidationException.class, "Username is already exist!.");
    }
    @Test
    void whenEmailExist_ThrowException() {
        // given
        RegisterRequest request = RegistrationHelper.EXIST_EMAIL;

        // when & then
        assertException(() -> registration.register(request),
                CredentialValidationException.class, "Email is already exist!.");
    }
}
