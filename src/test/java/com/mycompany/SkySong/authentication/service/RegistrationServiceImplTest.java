package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.exception.DatabaseException;
import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.exception.ServiceFailureException;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.repository.RoleDAO;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {
    private RegistrationServiceImpl registrationService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl(userDAO, roleDAO, passwordEncoder);
    }
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
    void shouldThrowExceptionWhenPasswordIsToShort() {
        RegisterRequest registerRequest =
                new RegisterRequest("testUsername", "testEmail@gmail.com", "Test@2");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowExceptionForInvalidPasswordFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "invalidPassword");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageForInvalidPasswordFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "invalidPassword");

        Exception exception = assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        String expectedMessage = "Invalid password format. The password must contain an least 8 characters, " +
                "including uppercase letters, lowercase letters, numbers, and special characters.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionForInvalidUsernameFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "invalidUsernameFormat#", "testEmail@gmail.com", "testPassword@123");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageForInvalidUsernameFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "invalidUsernameFormat#", "testEmail@gmail.com", "testPassword@123");

        Exception exception = assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        String expectedMessage = "Invalid username format. The username can contain only letter and numbers.";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "invalidEmail", "testPassword@123");

        assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageForInvalidEmailFormat() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "invalidEmail", "testPassword@123");

        Exception exception = assertThrows(RegisterException.class, () -> registrationService.register(registerRequest));

        String expectedMessage = "Invalid email address format." +
                " The email should follow the standard format (e.g., user@example.com).";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenRoleNotSetInTheDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(roleDAO.findByName(any())).thenReturn(Optional.empty());

        assertThrows(ServiceFailureException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenRoleNotSetInTheDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        Exception exception = assertThrows(ServiceFailureException.class,
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

        when(passwordEncoder.encode(any())).thenThrow(new ServiceFailureException("Service error"));

        assertThrows(ServiceFailureException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenPasswordEncodingFails() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(passwordEncoder.encode(registerRequest.password())).thenThrow(
                new ServiceFailureException("Service error"));
        Exception exception = assertThrows(ServiceFailureException.class,
                () -> registrationService.register(registerRequest));

        String expectedMessage = "There was an issue during password encoding. Please try again later.";

        assertEquals(expectedMessage, exception.getMessage());
    }
}
