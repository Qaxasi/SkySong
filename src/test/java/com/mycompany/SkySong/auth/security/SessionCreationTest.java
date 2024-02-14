package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.SessionExistenceChecker;
import com.mycompany.SkySong.SessionFetcher;
import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionCreationTest extends BaseIT {

    @Autowired
    private SessionCreation session;
    @Autowired
    private SessionExistenceChecker checker;
    @Autowired
    private SessionFetcher sessionFetcher;
    @Autowired
    private TokenHasher tokenHasher;

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
    void whenCreatingSession_SetsCorrectExpirationTime() {
        Instant now = Instant.now();

        String token = session.createSession(10);
        String hashedToken = tokenHasher.generateHashedToken(token);

        Session createdSession = sessionFetcher.getSession(hashedToken).orElseThrow(
                () -> new AssertionError("Expected session not found in database."));

        Instant expirationTime = createdSession.getExpiresAt().toInstant();
        Duration duration = Duration.between(now, expirationTime);
        long expectedDurationHours = 24;
        
        assertThat(duration.toHours() >= expectedDurationHours &&
                duration.minusHours(expectedDurationHours).toMinutes() <= 1).isTrue();
    }

    @Test
    void whenCreatingSession_AssignsAppropriateUserId() {
        String token = session.createSession(10);
        String hashedToken = tokenHasher.generateHashedToken(token);

        Integer userId = sessionFetcher.getSession(hashedToken).map(Session::getUserId).orElse(null);

        assertThat(userId).isEqualTo(10);
    }

    @Test
    void whenCreatingSession_NotStoreRawTokenInDatabase() {
        String token = session.createSession(10);
        assertThat(checker.sessionExist(token)).isFalse();
    }

    @Test
    void whenCreatingSession_StoreHashedTokenInDatabase() {
        String token = session.createSession(10);
        String hashedToken = tokenHasher.generateHashedToken(token);
        assertThat(checker.sessionExist(hashedToken)).isTrue();
    }
}
