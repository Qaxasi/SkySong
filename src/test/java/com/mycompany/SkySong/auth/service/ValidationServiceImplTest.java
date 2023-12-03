package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceImplTest {
    @InjectMocks
    private ValidationServiceImpl validationService;
    @Mock
    private ApplicationMessageService messageService;
    @Test
    void shouldThrowExceptionWhenPasswordIsToShort() {
        RegisterRequest registerRequest =
                new RegisterRequest("testUsername", "testEmail@gmail.com", "Test@2");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenPasswordDoesNotHaveUppercaseLetter() {
        RegisterRequest registerRequest =
                new RegisterRequest("testUsername", "testEmail@gmail.com", "testpass@123");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenPasswordDoesNotHaveLowercaseLetter() {
        RegisterRequest registerRequest =
                new RegisterRequest("testUsername", "testEmail@gmail.com", "TESTPASS@123");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenPasswordDoesNotHaveNumber() {
        RegisterRequest registerRequest =
                new RegisterRequest("testUsername", "testEmail@gmail.com", "testPass@ONE");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenPasswordDoesNotHaveSpecialCharacter() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword123");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageForInvalidPasswordFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "invalidPassword");

        String expectedMessage = "Invalid password format. The password must contain an least 8 characters, " +
                "including uppercase letters, lowercase letters, numbers, and special characters.";

        when(messageService.getMessage("validation.password.error")).thenReturn(expectedMessage);

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> validationService.validateCredentials(registerRequest));

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenUsernameIsToShort() {
        RegisterRequest registerRequest = new RegisterRequest(
                "un", "testEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenUsernameIsToLong() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testVeryLongUsernameFormat", "testEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowExceptionWhenUsernameContainSpecialCharacter() {
        RegisterRequest registerRequest = new RegisterRequest(
                "test#Username", "testEmail@gmail.com", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageForInvalidUsernameFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "invalidUsername#", "testEmail@gmail.com", "testPassword@123");

        String expectedMessage = "Invalid username format. The username can contain only letters and numbers," +
                " and should be between 3 to 20 characters long.";

        when(messageService.getMessage("validation.username.error")).thenReturn(expectedMessage);

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> validationService.validateCredentials(registerRequest));

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "invalidEmail", "testPassword@123");

        assertThrows(CredentialValidationException.class, () -> validationService.validateCredentials(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageForInvalidEmailFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "invalidEmail", "testPassword@123");

        String expectedMessage = "Invalid email address format. The email should follow the standard " +
                "format (e.g., user@example.com) and be between 6 to 30 characters long.";

        when(messageService.getMessage("validation.email.error")).thenReturn(expectedMessage);

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> validationService.validateCredentials(registerRequest));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
