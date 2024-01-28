package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
