package com.mycompany.SkySong.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenHasherTest {

    private TokenHasher tokenHasher;

    @BeforeEach
    void setUp() {
        tokenHasher = new TokenHasher();
    }

    @Test
    void WhenTokenIsConsistent_ReturnsConsistentHash() {
        String token = "test-token";
        String firstHash = tokenHasher.hashToken(token);
        String secondHash = tokenHasher.hashToken(token);

        assertThat(firstHash).isEqualTo(secondHash);
    }

    @Test
    void whenTokenAreDifferent_ReturnUniqueHashes() {
        String firstToken = "first-token";
        String secondToken = "second-token";

        String firstHash = tokenHasher.hashToken(firstToken);
        String secondHash = tokenHasher.hashToken(secondToken);

        assertThat(firstHash).isNotEqualTo(secondHash);
    }
}
