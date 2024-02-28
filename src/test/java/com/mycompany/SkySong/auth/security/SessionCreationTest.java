package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.testsupport.auth.security.SessionExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.security.SessionFetcher;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionCreationTest {

    @Test
    void whenCreatingSession_SetsCorrectExpirationTime() {
        Instant now = Instant.now();
        int userId = 10;

        String token = sessionCreation.createSession(userId);
        String hashedToken = tokenHasher.generateHashedToken(token);

        Session createdSession = sessionFetcher.getSession(hashedToken).orElseThrow(
                () -> new AssertionError("Expected session not found in database."));
        Instant expirationTime = createdSession.getExpiresAt().toInstant();

        long hoursUntilExpiration = Duration.between(now, expirationTime).toHours();
        assertThat(hoursUntilExpiration).isBetween(23L, 25L);
    }

}
