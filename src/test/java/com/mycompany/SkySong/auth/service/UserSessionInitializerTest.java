package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSessionInitializerTest extends BaseIT {

    @Autowired
    private UserSessionInitializer sessionInitializer;

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
    void whenInitializeSession_SessionNotNull() {
        Session session = sessionInitializer.initializeSession(LoginRequests.VALID_CREDENTIALS);
        assertThat(session).isNotNull();
    }
    
    @Test
    void whenInitializeSession_UserIdIsCorrect() {
        Session session = sessionInitializer.initializeSession(LoginRequests.VALID_CREDENTIALS);
        assertThat(session.getUserId()).isEqualTo(1);
    }

    @Test
    void whenInitializeSession_SessionIdNotNull() {
        Session session = sessionInitializer.initializeSession(LoginRequests.VALID_CREDENTIALS);
        assertThat(session.getSessionId()).isNotNull();
    }
}
