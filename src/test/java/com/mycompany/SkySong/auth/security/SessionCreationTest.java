package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.SessionExistenceChecker;
import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionCreationTest extends BaseIT {

    @Autowired
    private SessionCreation session;
    @Autowired
    private SessionExistenceChecker checker;

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
    void whenCreatingSession_ReturnsNonNullToken() {
        String token = session.createSession(10);
        assertThat(token).isNotNull();
    }

    @Test
    void whenCreatingSession_NotStoreRawTokenInDatabase() {
        String token = session.createSession(10);
        assertThat(checker.sessionExist(token)).isFalse();
    }
}
