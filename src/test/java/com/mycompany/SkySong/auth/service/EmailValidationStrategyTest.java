package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailValidationStrategyTest {

    private ApplicationMessageService message;

    private EmailValidationStrategy strategy;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageServiceImpl();
        strategy = new EmailValidationStrategy(message);
    }

    @Test
    void whenEmailInvalidFormat_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.EMAIL_INVALID_FORMAT));
    }

    @Test
    void whenEmailToShort_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.EMAIL_TO_SHORT));
    }

    @Test
    void whenEmailToLong_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.EMAIL_TO_LONG));
    }
}
