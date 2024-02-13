package com.mycompany.SkySong.auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenHasherTest {

    private TokenHasher tokenHasher;

    @BeforeEach
    void setUp() {
        tokenHasher = new TokenHasherImpl();
    }

    @Test
    void WhenTokenIsConsistent_ReturnsConsistentHash() {
        String token = "test-token";
        String firstHash = tokenHasher.generateHashedToken(token);
        String secondHash = tokenHasher.generateHashedToken(token);

        assertThat(firstHash).isEqualTo(secondHash);
    }


}
