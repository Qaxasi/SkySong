package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.auth.service.UserLoginHelper;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.mycompany.SkySong.testsupport.assertions.ExceptionAssertionUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationServiceTest extends BaseIT {

    @Autowired
    private UserRegistrationService registration;
    @Autowired
    private RegistrationRequests registrationHelper;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenRegistrationSuccess_ReturnMessage () {
        ApiResponse response = registration.register(registrationHelper.validCredentials);
        assertEquals("User registered successfully." , response.message());
    }

    @Test
    void whenRegistrationSuccess_AllowLoginForRegisteredUser() {
        registration.register(registrationHelper.validCredentials);
        assertNotNull(login.loginRegisteredUser());
    }

    @Test
    void whenInvalidUsernameFormat_ThrowException() {
        assertException(() -> registration.register(registrationHelper.usernameToLong),
                CredentialValidationException.class, "Invalid username format. The username can contain only letters" +
                        " and numbers, and should be between 3 and 20 characters long.");
    }

    @Test
    void whenInvalidEmailFormat_ThrowException() {
        assertException(() -> registration.register(registrationHelper.emailInvalidFormat),
                CredentialValidationException.class, "Invalid email address format. The email should follow the " +
                        "standard format (e.g., user@example.com) and be between 6 and 30 characters long.");
    }

    @Test
    void whenInvalidPasswordFormat_ThrowException() {
        assertException(() -> registration.register(registrationHelper.passwordNoNumber),
                CredentialValidationException.class, "Invalid password format. The password must contain an least 8 " +
                        "characters, including uppercase letters, lowercase letters, numbers, and special characters.");
    }

    @Test
    void whenUsernameExist_ThrowException() {
        assertException(() -> registration.register(registrationHelper.existUsername),
                CredentialValidationException.class, "Username is already exist!.");
    }

    @Test
    void whenEmailExist_ThrowException() {
        assertException(() -> registration.register(registrationHelper.existEmail),
                CredentialValidationException.class, "Email is already exist!.");
    }
}
