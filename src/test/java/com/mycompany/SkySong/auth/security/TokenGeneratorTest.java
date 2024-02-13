package com.mycompany.SkySong.auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenGeneratorTest {

    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        tokenGenerator = new TokenGeneratorImpl();
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
}
