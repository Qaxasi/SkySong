package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.auth.security.UserSessionChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSessionCreatorTest extends BaseIT {

    @Autowired
    private UserSessionCreator userSessionCreator;
    @Autowired
    private LoginRequests loginHelper;
    @Autowired
    private UserSessionChecker sessionChecker;

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
    void whenAuthenticationFails_SessionNotCreated() {
        userSessionCreator.createUserSession(loginHelper.loginInvalidPassword("User"));
        assertThat(sessionChecker.userHasSession("User")).isFalse();
    }

    @Test
    void whenSessionCreated_SessionIsStored() {
        userSessionCreator.createUserSession(loginHelper.login("User"));
        assertThat(sessionChecker.userHasSession("User")).isTrue();
    }

    @Test
    void whenSessionCreated_SessionIdNotNull() {
        Session session = userSessionCreator.createUserSession(loginHelper.login("User"));
        assertThat(session.getSessionId()).isNotNull();
    }
}
