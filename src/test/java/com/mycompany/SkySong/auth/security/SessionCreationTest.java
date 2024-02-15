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
    private SessionCreation sessionCreation;
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
        int userId = 10;
        String token = sessionCreation.createSession(userId);
        assertThat(token).isNotNull();
    }

    @Test
    void whenCreatingSession_SetsCorrectExpirationTime() {
        Instant now = Instant.now();
        int userId = 10;

        String token = sessionCreation.createSession(userId);
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
        int userId = 10;

        String token = sessionCreation.createSession(userId);
        String hashedToken = tokenHasher.generateHashedToken(token);

        Integer retrievedUserId = sessionFetcher.getSession(hashedToken)
                .map(Session::getUserId)
                .orElse(null);

        assertThat(retrievedUserId).isEqualTo(10);
    }

    @Test
    void whenCreatingSession_NotStoreRawTokenInDatabase() {
        int userId = 10;
        String token = sessionCreation.createSession(userId);
        assertThat(checker.sessionExist(token)).isFalse();
    }

    @Test
    void whenCreatingSession_StoreHashedTokenInDatabase() {
        int userId = 10;
        String token = sessionCreation.createSession(userId);
        String hashedToken = tokenHasher.generateHashedToken(token);
        assertThat(checker.sessionExist(hashedToken)).isTrue();
    }
}
