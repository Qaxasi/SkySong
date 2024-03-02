package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RegistrationValidationTest extends BaseIT {

    @Autowired
    private RegistrationValidation validation;
    @Autowired
    private RegistrationRequests registrationHelper;

    @Test
    void whenValidCredentials_NotThrowException() {
        assertDoesNotThrow(() -> validation.validateRequest(RegistrationRequests.VALID_CREDENTIALS));
    }
}
