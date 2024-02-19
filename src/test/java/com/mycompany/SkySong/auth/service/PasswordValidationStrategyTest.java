package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
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
    void whenPasswordWithoutUppercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.PASSWORD_NO_UPPERCASE_LETTER));
    }

    @Test
    void whenPasswordWithoutLowercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.PASSWORD_NO_LOWERCASE_LETTER));
    }

    @Test
    void whenPasswordWithoutNumber_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.PASSWORD_NO_NUMBER));
    }

    @Test
    void whenPasswordWithoutSpecialCharacter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.PASSWORD_NO_SPECIAL_CHARACTER));
    }
}
