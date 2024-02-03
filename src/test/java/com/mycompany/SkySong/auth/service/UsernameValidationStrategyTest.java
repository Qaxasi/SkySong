package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.shared.service.ApplicationMessageServiceImpl;
import com.mycompany.SkySong.testsupport.auth.RegistrationRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsernameValidationStrategyTest {
    private ApplicationMessageService message;
    private UsernameValidationStrategy strategy;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageServiceImpl();
        strategy = new UsernameValidationStrategy(message);
    }
    @Test
    void whenUsernameIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(RegistrationRequests.USERNAME_TO_SHORT));
    }


}
