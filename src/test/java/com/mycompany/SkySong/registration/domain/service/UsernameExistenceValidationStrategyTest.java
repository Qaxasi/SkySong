package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.domain.service.UsernameExistenceValidationStrategy;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsernameExistenceValidationStrategyTest extends BaseIT {

    @Autowired
    private UsernameExistenceValidationStrategy strategy;
    @Autowired
    private RegistrationRequests registrationHelper;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenUsernameExist_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.existUsername));
    }

    @Test
    void whenUsernameNotExist_NotThrowException() {
        assertDoesNotThrow(() -> strategy.validate(registrationHelper.uniqueUsername));
    }
}
