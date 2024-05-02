package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import com.mycompany.SkySong.registration.domain.service.UsernameValidationStrategy;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsernameValidationStrategyTest {
    private ApplicationMessageLoader message;
    private UsernameValidationStrategy strategy;
    private RegistrationRequests registrationHelper;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageLoader();
        strategy = new UsernameValidationStrategy(message);
        registrationHelper = new RegistrationRequests();
    }

    @Test
    void whenUsernameIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.usernameToShort));
    }

    @Test
    void whenUsernameIsToLong_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.usernameToLong));
    }

    @Test
    void whenUsernameContainsSpecialCharacter_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.usernameWithSpecialCharacter));
    }
}
