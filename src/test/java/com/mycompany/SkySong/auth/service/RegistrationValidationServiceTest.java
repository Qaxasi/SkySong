package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.testsupport.auth.RegistrationRequests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ValidationServiceTest {

    @Autowired
    private ValidationService validationService;

    @Test
    void whenPasswordIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> validationService.validateCredentials(RegistrationRequests.PASSWORD_TO_SHORT));
    }
    @Test
    void whenPasswordNoHaveUppercaseLetter_ThrowException() {
        RegisterRequest registerRequest =
                new RegisterRequest("User", "mail@mail.com", "password#3");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void whenPasswordNoHaveLowercaseLetter_ThrowException() {
        RegisterRequest registerRequest =
                new RegisterRequest("User", "mail@mail.com", "PASSWORD#3");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void whenPasswordNoHaveNumber_ThrowException() {
        RegisterRequest registerRequest =
                new RegisterRequest("User", "mail@mmail.com", "Password#d");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void whenPasswordNoHaveSpecialCharacter_ThrowException() {
        RegisterRequest registerRequest = new RegisterRequest(
                "User", "mail@mail.com", "Password3");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
//    @Test
//    void shouldThrowErrorMessageForInvalidPasswordFormat() {
//        RegisterRequest registerRequest = new RegisterRequest(
//                "testUsername", "testEmail@gmail.com", "invalidPassword");
//
//        String expectedMessage = "Invalid password format. The password must contain an least 8 characters, " +
//                "including uppercase letters, lowercase letters, numbers, and special characters.";
//
//        when(messageService.getMessage("validation.password.error")).thenReturn(expectedMessage);
//
//        Exception exception = assertThrows(CredentialValidationException.class,
//                () -> validationService.validateCredentials(registerRequest));
//
//        assertEquals(expectedMessage, exception.getMessage());
//    }
//    @Test
//    void shouldThrowExceptionWhenUsernameIsToShort() {
//        RegisterRequest registerRequest = new RegisterRequest(
//                "un", "testEmail@gmail.com", "testPassword@123");
//
//        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
//    }
//    @Test
//    void shouldThrowExceptionWhenUsernameIsToLong() {
//        RegisterRequest registerRequest = new RegisterRequest(
//                "testVeryLongUsernameFormat", "testEmail@gmail.com", "testPassword@123");
//
//        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
//    }
//    @Test
//    void shouldThrowExceptionWhenUsernameContainSpecialCharacter() {
//        RegisterRequest registerRequest = new RegisterRequest(
//                "test#Username", "testEmail@gmail.com", "testPassword@123");
//
//        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
//    }
//    @Test
//    void shouldThrowErrorMessageForInvalidUsernameFormat() {
//        RegisterRequest registerRequest = new RegisterRequest(
//                "invalidUsername#", "testEmail@gmail.com", "testPassword@123");
//
//        String expectedMessage = "Invalid username format. The username can contain only letters and numbers," +
//                " and should be between 3 to 20 characters long.";
//
//        when(messageService.getMessage("validation.username.error")).thenReturn(expectedMessage);
//
//        Exception exception = assertThrows(CredentialValidationException.class,
//                () -> validationService.validateCredentials(registerRequest));
//
//        assertEquals(expectedMessage, exception.getMessage());
//    }
//    @Test
//    void shouldThrowExceptionForInvalidEmailFormat() {
//        RegisterRequest registerRequest = new RegisterRequest(
//                "testUsername", "invalidEmail", "testPassword@123");
//
//        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
//    }
//    @Test
//    void shouldThrowErrorMessageForInvalidEmailFormat() {
//        RegisterRequest registerRequest = new RegisterRequest(
//                "testUsername", "invalidEmail", "testPassword@123");
//
//        String expectedMessage = "Invalid email address format. The email should follow the standard " +
//                "format (e.g., user@example.com) and be between 6 to 30 characters long.";
//
//        when(messageService.getMessage("validation.email.error")).thenReturn(expectedMessage);
//
//        Exception exception = assertThrows(CredentialValidationException.class,
//                () -> validationService.validateCredentials(registerRequest));
//
//        assertEquals(expectedMessage, exception.getMessage());
//    }
}
