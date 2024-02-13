package com.mycompany.SkySong.auth.security;

import org.junit.jupiter.api.BeforeEach;

public class TokenGeneratorTest {

    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        tokenGenerator = new TokenGeneratorImpl();
    }
}
