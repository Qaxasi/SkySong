package com.mycompany.SkySong.logout;

import com.mycompany.SkySong.logout.domain.service.SessionDeletionService;
import com.mycompany.SkySong.testsupport.auth.security.SessionChecker;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SessionDeletionServiceTest extends BaseIT {

    @Autowired
    private SessionDeletionService sessionDeletionService;
    @Autowired
    private SessionChecker checker;

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
    void whenSessionExist_DeletesSession() {
        String sessionId = "jrYa_WLToysV-r08qLhwUZncJLY8OPgT";
        sessionDeletionService.deleteSession(sessionId);
        assertThat(checker.sessionExist("jrYa_WLToysV-r08qLhwUZncJLY8OPgT")).isFalse();
    }

    @Test
    void whenSessionNotExist_DeleteOperationCompletesGracefully() {
        String sessionId = "xyz";
        assertDoesNotThrow(() -> sessionDeletionService.deleteSession(sessionId));
    }
}
