package com.mycompany.SkySong.auth.security;

import org.junit.jupiter.api.BeforeEach;

public class TokenHasherTest {

    private TokenHasher tokenHasher;

    @BeforeEach
    void setUp() {
        tokenHasher = new TokenHasherImpl();
    }

    
}
