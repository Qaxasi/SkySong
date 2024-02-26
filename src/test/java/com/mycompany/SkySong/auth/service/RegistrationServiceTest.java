package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.mycompany.SkySong.testsupport.assertions.ExceptionAssertionUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationServiceTest extends BaseIT {

    @Autowired
    private RegistrationService registration;
    @Autowired
    private LoginService login;

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
    void whenRegistrationSuccess_AllowLoginForRegisterUser() {
        registration.register(RegistrationRequests.VALID_CREDENTIALS);
        assertNotNull(login.login(LoginRequests.LOGIN_REGISTERED_USER));
    }

    @Test
    void whenRegistrationSuccess_ReturnMessage () {
        ApiResponse response = registration.register(RegistrationRequests.VALID_CREDENTIALS);
        assertEquals("User registered successfully." , response.message());
    }

    @Test
    void whenInvalidUsernameFormat_ThrowException() {
        assertException(() -> registration.register(RegistrationRequests.USERNAME_TO_SHORT),
                CredentialValidationException.class, "Invalid username format. The username can contain only letters" +
                        " and numbers, and should be between 3 and 20 characters long.");
    }

    @Test
    void whenInvalidEmailFormat_ThrowException() {
        assertException(() -> registration.register(RegistrationRequests.EMAIL_TO_SHORT),
                CredentialValidationException.class, "Invalid email address format. The email should follow the " +
                        "standard format (e.g., user@example.com) and be between 6 and 30 characters long.");
    }

    @Test
    void whenInvalidPasswordFormat_ThrowException() {
        assertException(() -> registration.register(RegistrationRequests.PASSWORD_NO_NUMBER),
                CredentialValidationException.class, "Invalid password format. The password must contain an least 8 " +
                        "characters, including uppercase letters, lowercase letters, numbers, and special characters.");
    }

    @Test
    void whenUsernameExist_ThrowException() {
        assertException(() -> registration.register(RegistrationRequests.EXIST_USERNAME),
                CredentialValidationException.class, "Username is already exist!.");
    }

    @Test
    void whenEmailExist_ThrowException() {
        assertException(() -> registration.register(RegistrationRequests.EXIST_EMAIL),
                CredentialValidationException.class, "Email is already exist!.");
    }
}
