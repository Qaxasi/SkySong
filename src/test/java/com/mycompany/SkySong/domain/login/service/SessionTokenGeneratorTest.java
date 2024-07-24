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

    @Test
    void whenGeneratingMultipleTokens_ReturnsUniqueTokens() {
        String firstToken = tokenGenerator.generateToken();
        String secondToken = tokenGenerator.generateToken();

        assertThat(firstToken).isNotEqualTo(secondToken);
    }

    @Test
    void whenGeneratingToken_TokenHaveExpectedLength() {
        String token = tokenGenerator.generateToken();

        assertThat(token.length()).isSameAs(32);
    }
}
