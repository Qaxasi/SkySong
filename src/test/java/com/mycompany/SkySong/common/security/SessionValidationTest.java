package com.mycompany.SkySong.common.security;

import com.mycompany.SkySong.common.security.SessionValidation;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.security.SessionTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionValidationTest extends BaseIT {

    @Autowired
    private SessionValidation validation;
    @Autowired
    private SessionTestHelper sessionHelper;

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
    void whenValidSession_ReturnsTrue() {
        String sessionId = "IUb2QMs9iSPMdcOJIvJwH5Q5FA6ayCz1";
        assertThat(validation.validateSession(sessionId)).isTrue();
    }

    @Test
    void whenSessionNotExist_ReturnFalse() {
        String sessionId = "xyz";
        assertThat(validation.validateSession(sessionId)).isFalse();
    }

    @Test
    void whenSessionIsExpired_ReturnFalse() {
        int userId = 10;
        String sessionId = sessionHelper.createExpiredSession(userId);
        assertThat(validation.validateSession(sessionId)).isFalse();
    }
}
