package com.mycompany.SkySong.domain.login.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTokenGeneratorTest {

    private SessionTokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        tokenGenerator = new SessionTokenGenerator();
    }

    @Test
    void whenGeneratingToken_ReturnsNonEmptyString() {
        String token = tokenGenerator.generateToken();
        assertThat(token).isNotNull();
    }
}
