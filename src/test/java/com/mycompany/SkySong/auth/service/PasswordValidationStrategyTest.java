package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.shared.service.ApplicationMessageServiceImpl;
import com.mycompany.SkySong.testsupport.auth.RegistrationRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordValidationStrategyTest {

    private ApplicationMessageService message;

    private PasswordValidationStrategy strategy;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageServiceImpl();
        strategy = new PasswordValidationStrategy(message);
    }

    @Test
    void whenPasswordIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.PASSWORD_TO_SHORT));
    }

    @Test
    void whenPasswordNoHaveUppercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.PASSWORD_NO_UPPERCASE_LETTER));
    }
}
