package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.h2.api.CredentialsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CredentialExistenceCheckerImplTest {
    @InjectMocks
    private CredentialExistenceCheckerImpl credentialExistenceChecker;
    @Mock
    private UserDAO userDAO;
    @Mock
    private ApplicationMessageService messageService;
    @Test
    void shouldThrowExceptionIfUsernameExists() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername("testUsername")).thenReturn(true);

        assertThrows(CredentialValidationException.class,
                () -> credentialExistenceChecker.checkForExistingCredentials(registerRequest));
    }
    @Test
    void shouldThrowExceptionIfEmailExist() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByEmail("testEmail@gmail.com")).thenReturn(true);

        assertThrows(CredentialValidationException.class,
                () -> credentialExistenceChecker.checkForExistingCredentials(registerRequest));
    }
    @Test
    void shouldReturnErrorMessageWhenUsernameExist() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername("testUsername")).thenReturn(true);

        String expectedMessage = "Username is already exist!.";
        when(messageService.getMessage("username.exist")).thenReturn(expectedMessage);

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> credentialExistenceChecker.checkForExistingCredentials(registerRequest));

        assertEquals(exception.getMessage(), expectedMessage);
    }
    @Test
    void shouldReturnErrorMessageWhenEmailExist() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByEmail("testEmail@gmail.com")).thenReturn(true);

        String expectedMessage = "Email is already exist!.";
        when(messageService.getMessage("email.exist")).thenReturn(expectedMessage);

        Exception exception = assertThrows(CredentialValidationException.class,
                () -> credentialExistenceChecker.checkForExistingCredentials(registerRequest));

        assertEquals(exception.getMessage(), expectedMessage);
    }
}
