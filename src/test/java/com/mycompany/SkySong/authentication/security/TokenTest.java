package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.exception.TokenException;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;


import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenTest {
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private Authentication mockAuth;

    @BeforeEach
    public void setUp() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());
        jwtTokenProvider = new JwtTokenProvider(base64Secret, 1000L);
    }
    @Test
    void shouldGenerateValidTokenForGivenAuthentication() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProvider.generateToken(mockAuth);
        assertNotNull(token);
    }
    @Test
    void shouldValidateTokenGeneratedForGivenAuthentication() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProvider.generateToken(mockAuth);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void shouldRetrieveUsernameFromValidToken() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProvider.generateToken(mockAuth);
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        String retrieveUsername = claims.getSubject();

        assertEquals("testUser", retrieveUsername);
    }

    @Test
    void shouldThrowExceptionForExpiredToken() throws InterruptedException {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProvider.generateToken(mockAuth);
        Thread.sleep(1001);
        assertThrows(TokenException.class, () -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void shouldThrowTokenExceptionForMalformedToken() {
        String malformedToken = "not-valid-token";

        assertThrows(TokenException.class, () -> jwtTokenProvider.validateToken(malformedToken));
    }
    @Test
    void shouldThrowTokenExceptionForEmptyClaims() {
        String emptyClaimsToken = "";

        assertThrows(TokenException.class, () -> jwtTokenProvider.validateToken(emptyClaimsToken));
    }
    @Test
    void shouldThrowTokenExceptionForInvalidAlgorithm() {
        String unsupportedToken = Jwts.builder()
                .setSubject("testUser")
                .setExpiration(new Date(System.currentTimeMillis() + 100L))
                .compact();

        assertThrows(TokenException.class, () -> jwtTokenProvider.validateToken(unsupportedToken));
    }
}