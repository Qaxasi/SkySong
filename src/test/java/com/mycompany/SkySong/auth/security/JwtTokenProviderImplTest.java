package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
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
public class JwtTokenProviderImplTest {
    private JwtTokenProviderImpl jwtTokenProvider;
    @Mock
    private Authentication mockAuth;
    @Mock
    private JwtExceptionHandler jwtExceptionHandler;
    private Key key;

    @BeforeEach
    public void setUp() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());
        long jwtExpirationTime = 1000L;

        jwtTokenProvider = new JwtTokenProviderImpl(base64Secret, jwtExpirationTime, jwtExceptionHandler);
    }
    @Test
    void shouldGenerateTokenForGivenAuthentication() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProvider.generateToken(mockAuth);
        assertNotNull(token);
        assertFalse(token.isEmpty());
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
    void shouldRetrieveExpirationTimeFromToken() {
        String expectedUser = "testUser";

        when(mockAuth.getName()).thenReturn(expectedUser);

        String token = jwtTokenProvider.generateToken(mockAuth);
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);

        assertNotNull(claims.getExpiration());
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