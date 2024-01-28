package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static com.mycompany.SkySong.testsupport.auth.service.CredentialExistenceCheckerImplTestHelper.assertEmailException;
import static com.mycompany.SkySong.testsupport.auth.service.CredentialExistenceCheckerImplTestHelper.assertUsernameException;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CredentialExistenceCheckerTest extends BaseIT {
    @Autowired
    private CredentialExistenceChecker existenceChecker;
    @Test
    void whenUsernameExist_ThrowException() {
        RegisterRequest request = new RegisterRequest("Max", "mail@mail.com", "Password#3");

        assertThrows(CredentialValidationException.class,
                () -> existenceChecker.checkForExistingCredentials(request));

    }
    @Test
    void whenEmailExist_ThrowException() {
        RegisterRequest request = new RegisterRequest("User", "max@mail.com", "Password#3");

        assertThrows(CredentialValidationException.class,
                () -> existenceChecker.checkForExistingCredentials(request));
    }
}
