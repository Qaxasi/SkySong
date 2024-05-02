package com.mycompany.SkySong.registration.adapter.validation;

import com.mycompany.SkySong.registration.CredentialValidationException;
import com.mycompany.SkySong.registration.EmailExistenceValidationStrategy;
import com.mycompany.SkySong.user.User;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailExistenceValidationStrategyTest extends BaseIT {

    @Autowired
    private EmailExistenceValidationStrategy strategy;
    @Autowired
    private RegistrationRequests registrationHelper;
    @Autowired
    private UserBuilder userBuilder;

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
    void whenEmailExist_ThrowException() {
        User user = userBuilder.buildByEmail("user@mail.mail").build();
        assertThrows(CredentialValidationException.class,
                () -> strategy.validate(registrationHelper.registerByEmail("user@mail.mail")));
    }

    @Test
    void whenEmailNotExist_NotThrowException() {
        assertDoesNotThrow(() -> strategy.validate(registrationHelper.registerByEmail("user@mail.mail")));
    }
}
