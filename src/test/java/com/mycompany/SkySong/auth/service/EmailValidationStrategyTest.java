package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailValidationStrategyTest {

    private ApplicationMessageService message;

    private EmailValidationStrategy strategy;
    private RegistrationRequests registrationHelper;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageServiceImpl();
        strategy = new EmailValidationStrategy(message);
        registrationHelper = new RegistrationRequests();
    }

    @Test
    void whenEmailInvalidFormat_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.emailInvalidFormat));
    }

    @Test
    void whenEmailToShort_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.emailToShort));
    }

    @Test
    void whenEmailToLong_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.emailToLong));
    }
}
