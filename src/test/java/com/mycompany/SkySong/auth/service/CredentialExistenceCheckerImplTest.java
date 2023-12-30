package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mycompany.SkySong.testsupport.auth.service.CredentialExistenceCheckerImplTestHelper.assertEmailException;
import static com.mycompany.SkySong.testsupport.auth.service.CredentialExistenceCheckerImplTestHelper.assertUsernameException;

@ExtendWith(MockitoExtension.class)
public class CredentialExistenceCheckerImplTest {
    @InjectMocks
    private CredentialExistenceCheckerImpl credentialExistenceChecker;
    @Mock
    private UserDAO userDAO;
    @Mock
    private ApplicationMessageService messageService;
    @Test
    void whenUsernameExist_ThrowException() {
        assertUsernameException(
                userDAO, credentialExistenceChecker, "User", true, CredentialValidationException.class);
    }
    @Test
    void whenEmailExist_ThrowException() {
        assertEmailException(
                userDAO, credentialExistenceChecker, "mail@mail.com", true, CredentialValidationException.class);
    }
}
