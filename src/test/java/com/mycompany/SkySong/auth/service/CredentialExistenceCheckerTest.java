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

    @Test
    void whenUsernameExist_ThrowException() {
        // given
        RegisterRequest request = RegistrationHelper.EXIST_USERNAME;

        // when & then
        assertThrows(CredentialValidationException.class,
                () -> existenceChecker.checkForExistingCredentials(request));

    }
    @Test
    void whenEmailExist_ThrowException() {
        // given
        RegisterRequest request = RegistrationHelper.EXIST_EMAIL;

        // when & then
        assertThrows(CredentialValidationException.class,
                () -> existenceChecker.checkForExistingCredentials(request));
    }
}
