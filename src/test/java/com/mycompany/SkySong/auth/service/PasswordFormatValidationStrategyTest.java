package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordFormatValidationStrategyTest {

    private ApplicationMessageLoader message;

    private PasswordFormatValidationStrategy strategy;
    private RegistrationRequests registrationHelper;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageLoader();
        strategy = new PasswordFormatValidationStrategy(message);
        registrationHelper = new RegistrationRequests();
    }

    @Test
    void whenPasswordIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.passwordToShort));
    }

    @Test
    void whenPasswordWithoutUppercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.passwordNoUppercaseLetter));
    }

    @Test
    void whenPasswordWithoutLowercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.passwordNoLowercaseLetter));
    }

    @Test
    void whenPasswordWithoutNumber_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.passwordNoNumber));
    }

    @Test
    void whenPasswordWithoutSpecialCharacter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.passwordNoSpecialCharacter));
    }
}
