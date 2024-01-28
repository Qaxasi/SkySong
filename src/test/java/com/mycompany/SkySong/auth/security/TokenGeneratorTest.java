package com.mycompany.SkySong.auth.security;

import org.junit.jupiter.api.BeforeEach;

public class TokenGeneratorTest {
    private TokenGenerator tokenGenerator;
    private DateProvider dateProvider;
    private KeyManager keyManager;
    @BeforeEach
    void setUp() {
        keyManager = new KeyManagerImpl("1237tfdh47326hdhjee6e2redgd2dgf12wfsfd2e2iefdgger61gydb");
        dateProvider = new DateProvider();
        tokenGenerator = new TokenGeneratorImpl(1000L, dateProvider, keyManager);
    }
}

