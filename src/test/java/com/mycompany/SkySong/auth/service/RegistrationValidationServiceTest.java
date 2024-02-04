package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.auth.RegistrationRequests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegistrationValidationServiceTest {

    @Autowired
    private RegistrationValidationService validator;

}

