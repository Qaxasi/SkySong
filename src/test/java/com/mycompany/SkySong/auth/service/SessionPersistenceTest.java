package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.security.SessionPersistence;
import com.mycompany.SkySong.testsupport.auth.security.SessionExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.service.TestSessionCreator;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionPersistenceTest extends BaseIT {

    @Autowired
    private SessionPersistence sessionPersistence;
    @Autowired
    private SessionExistenceChecker sessionChecker;
    private TestSessionCreator sessionCreation;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        sessionCreation = new TestSessionCreator();
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenSaveSession_SessionExist() {
        Session session = sessionCreation.createSession();
        sessionPersistence.saveSession(session);
        assertThat(sessionChecker.sessionExist("xAUpqIbS2L9_ULU39L7Z007RJufNgFizawVK68qTyrw=")).isTrue();
    }
}
