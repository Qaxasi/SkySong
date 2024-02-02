package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.RegistrationRequests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CredentialExistenceCheckerTest extends BaseIT {
    @Autowired
    private CredentialExistenceChecker existenceChecker;
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
                () -> existenceChecker.checkForExistingCredentials(RegistrationRequests.EXIST_USERNAME));

    }
    @Test
    void whenEmailExist_ThrowException() {
        assertThrows(CredentialValidationException.class,
                () -> existenceChecker.checkForExistingCredentials(RegistrationRequests.EXIST_EMAIL));
    }
}
