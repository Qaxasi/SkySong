package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.exception.RegisterException;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void shouldThrowRegisterExceptionIfUsernameExists() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testUniqueEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername("testUsername")).thenThrow(new CredentialValidationException("Username exist!"));

        assertThrows(CredentialValidationException.class,
                () -> credentialExistenceChecker.checkForExistingCredentials(registerRequest));
    }
}
