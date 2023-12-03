package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.repository.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private ValidationService validationService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Test
    void shouldReturnCorrectMessageAfterSuccessfullyRegisterNewUserWithValidCredentials() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userDAO.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPassword");
        when(roleDAO.findByName(any())).thenReturn(Optional.of(new Role()));

        RegistrationResponse response = registrationService.register(registerRequest);

        String expectedMessage = "User registered successfully";
        assertEquals(response.message(), expectedMessage);
    }
    @Test
    void shouldThrowExceptionWhenTryRegisterWithExistUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "existUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(true);

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenTryRegisterWithExistUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "existUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(true);

        Exception exception = assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        String expectedMessage = "Username is already exist!.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenTryRegisterWithExistingEmail() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "existEmai@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userDAO.existsByEmail(registerRequest.email())).thenReturn(true);

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenTryRegisterWithExistingEmail() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "existEmai@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userDAO.existsByEmail(registerRequest.email())).thenReturn(true);

        Exception exception = assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        String expectedMessage = "Email is already exist!.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenRoleNotSetInTheDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(roleDAO.findByName(any())).thenReturn(Optional.empty());

        assertThrows(InternalErrorException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenRoleNotSetInTheDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        Exception exception = assertThrows(InternalErrorException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "There was an issue during registration. Please try again later.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenDatabaseErrorOccurs() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(roleDAO.findByName(any())).thenReturn(Optional.of(new Role()));
        when(userDAO.save(any())).thenThrow(new DataIntegrityViolationException("Database error"));

        assertThrows(DatabaseException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenDatabaseErrorOccurs() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(roleDAO.findByName(any())).thenReturn(Optional.of(new Role()));
        when(userDAO.save(any())).thenThrow(new DataIntegrityViolationException("Database error"));

        Exception exception = assertThrows(DatabaseException.class, () -> registrationService.register(registerRequest));

        String expectedMessage = "An error occurred while processing your request. Please try again later.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenPasswordEncodingFails() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(passwordEncoder.encode(any())).thenThrow(new InternalErrorException("Service error"));

        assertThrows(InternalErrorException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenPasswordEncodingFails() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(passwordEncoder.encode(registerRequest.password())).thenThrow(
                new InternalErrorException("Service error"));
        Exception exception = assertThrows(InternalErrorException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "There was an issue during password encoding. Please try again later.";

        assertEquals(expectedMessage, exception.getMessage());
    }
}
