package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionCreationTest {

    private TokenGenerator tokenGenerator;
    private TokenHasher tokenHasher;
    private SessionCreation sessionCreation;

    @BeforeEach
    void setUp() {
        tokenGenerator = new TokenGeneratorImpl();
        tokenHasher = new TokenHasherImpl();
        sessionCreation = new SessionCreationImpl(tokenGenerator, tokenHasher);
    }

    @Test
    void whenCreatingSession_SetsCorrectExpirationTime() {
        Instant now = Instant.now();
        int userId = 10;

        Session session = sessionCreation.createSession(userId);

        Instant expirationTime = session.getExpiresAt().toInstant();

        long hoursUntilExpiration = Duration.between(now, expirationTime).toHours();
        assertThat(hoursUntilExpiration).isBetween(23L, 25L);
    }

    @Test
    void whenCreatingSession_SetCorrectUserId() {
        int userId = 10;
        Session session = sessionCreation.createSession(userId);
        assertThat(session.getUserId()).isEqualTo(10);
    }

    @Test
    void whenCreatingSession_SetSessionId() {
        int userId = 10;
        Session session = sessionCreation.createSession(userId);
        assertThat(session.getSessionId()).isNotNull();
    }
}
