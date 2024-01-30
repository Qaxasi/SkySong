package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.RegistrationHelper;
import com.mycompany.SkySong.testsupport.common.DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    private RegistrationHelper registrationHelper;
    @Autowired
    private DatabaseHelper databaseHelper;

    @Test
    void whenValidCredentials_RegisterUser() {
        registration.register(RegistrationHelper.register("User"));
        assertTrue(databaseHelper.userExist("User"));
    }
    @Test 
    void whenRegistrationSuccess_AllowLoginForRegisterUser() {
        registration.register(RegistrationHelper.register);
        assertNotNull(RegistrationHelper.login);
    }
    @Test
    void whenRegistrationSuccess_AssignRoleUserToNewUser() {
        registration.register(RegistrationHelper.register("User"));
        assertTrue(databaseHelper.hasUserRole("User", UserRole.ROLE_USER.name()));
    }
    @Test
    void whenRegistrationSuccess_ReturnMessage () {
        ApiResponse response = registration.register(RegistrationHelper.register);
        assertEquals("User registered successfully." , response.message());
    }
    @Test
    void whenInvalidUsernameFormat_ThrowException() {
        assertException(() -> registration.register(RegistrationHelper.invalidUsername),
                CredentialValidationException.class, "Invalid username format. The username can contain only letters" +
                        " and numbers, and should be between 3 and 20 characters long.");
    }
    @Test
    void whenInvalidEmailFormat_ThrowException() {
        assertException(() -> registration.register(RegistrationHelper.invalidEmail),
                CredentialValidationException.class, "Invalid email address format. The email should follow the " +
                        "standard format (e.g., user@example.com) and be between 6 and 30 characters long.");
    }
    @Test
    void whenInvalidPasswordFormat_ThrowException() {
        assertException(() -> registration.register(RegistrationHelper.invalidPassword),
                CredentialValidationException.class, "Invalid password format. The password must contain an least 8 " +
                        "characters, including uppercase letters, lowercase letters, numbers, and special characters.");
    }
    @Test
    void whenUsernameExist_ThrowException() {
        assertException(() -> registration.register(RegistrationHelper.existingUsername),
                CredentialValidationException.class, "Username is already exist!.");
    }
    @Test
    void whenEmailExist_ThrowException() {
        assertException(() -> registration.register(RegistrationHelper.existEmail),
                CredentialValidationException.class, "Email is already exist!.");
    }
}
