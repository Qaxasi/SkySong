package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @Test
    void whenTokenGenerated_ContainsExpectedSubject() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken("User", null);

        // when
        String token = tokenGenerator.generateToken(authentication);
        Jws<Claims> parsedToken = Jwts.parserBuilder()
                .setSigningKey(keyManager.getKey())
                .build()
                .parseClaimsJws(token);

        // then
        assertEquals("User", parsedToken.getBody().getSubject());
    }
    @Test
    void whenTokenGenerated_ContainsFutureExpirationTime() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken("User", null);

        // when
        String token = tokenGenerator.generateToken(authentication);
        Jws<Claims> parsedToken = Jwts.parserBuilder()
                .setSigningKey(keyManager.getKey())
                .build()
                .parseClaimsJws(token);

        // then
        assertTrue(parsedToken.getBody().getExpiration().after(new Date()));
    }
}

