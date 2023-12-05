package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private ValidationService validationService;
    @Mock
    private ApplicationMessageService messageService;
    @Mock
    private UserRoleManager userRoleManager;
    @Mock
    private UserFactory userFactory;
    @Mock
    private CredentialExistenceChecker credentialExistenceChecker;
    @Test
    void shouldThrowExceptionWhenRoleNotSetInTheDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userRoleManager.getRoleByName(any())).thenThrow(new InternalErrorException("test error"));

        assertThrows(InternalErrorException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenRoleNotSetInTheDatabase() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        String expectedMessage = "test error";

        when(userRoleManager.getRoleByName(any())).thenThrow(new InternalErrorException(expectedMessage));

        Exception exception = assertThrows(InternalErrorException.class,
                () -> registrationService.register(registerRequest));

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenDatabaseErrorOccurs() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword123");

        doNothing().when(validationService).validateCredentials(any(RegisterRequest.class));
        doNothing().when(credentialExistenceChecker).checkForExistingCredentials(any(RegisterRequest.class));
        when(userRoleManager.getRoleByName(UserRole.ROLE_USER)).thenReturn(new Role(UserRole.ROLE_USER));
        when(userFactory.createUser(any(), any())).thenReturn(new User());
        when(userDAO.save(any(User.class))).thenThrow(new DataAccessException("Database Error") {});

        assertThrows(DatabaseException.class, () -> registrationService.register(registerRequest));
    }
    @Test
    void shouldThrowErrorMessageWhenDatabaseErrorOccurs() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword123");

        String expectedMessage = "test message";

        doNothing().when(validationService).validateCredentials(any(RegisterRequest.class));
        doNothing().when(credentialExistenceChecker).checkForExistingCredentials(any(RegisterRequest.class));
        when(userRoleManager.getRoleByName(UserRole.ROLE_USER)).thenReturn(new Role(UserRole.ROLE_USER));
        when(userFactory.createUser(any(), any())).thenReturn(new User());
        when(messageService.getMessage("user.registration.error")).thenReturn(expectedMessage);
        when(userDAO.save(any(User.class))).thenThrow(new DataAccessException("Database Error") {});

        Exception exception = assertThrows(DatabaseException.class,
                () -> registrationService.register(registerRequest));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
