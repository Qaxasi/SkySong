package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.RegistrationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CredentialExistenceCheckerTest extends BaseIT {
    @Autowired
    private CredentialExistenceChecker existenceChecker;
    @Autowired
    private RegistrationHelper registrationHelper;
    @Test
    void whenUsernameExist_ThrowException() {
        // given
        RegisterRequest request = registrationHelper.existUsername;

        // when & then
        assertThrows(CredentialValidationException.class,
                () -> existenceChecker.checkForExistingCredentials(request));

    }
    @Test
    void whenEmailExist_ThrowException() {
        // given
        RegisterRequest request = registrationHelper.existEmail;

        // when & then
        assertThrows(CredentialValidationException.class,
                () -> existenceChecker.checkForExistingCredentials(request));
    }
}
