package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.auth.service.JwtTokenProviderImpl;
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
    private JwtTokenProviderImpl jwtTokenProviderImpl;
    @Mock
    private Authentication mockAuth;

    @BeforeEach
    public void setUp() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());
        jwtTokenProviderImpl = new JwtTokenProviderImpl(base64Secret, 1000L);
    }
    @Test
    void shouldGenerateTokenForGivenAuthentication() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProviderImpl.generateToken(mockAuth);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    @Test
    void shouldValidateTokenGeneratedForGivenAuthentication() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProviderImpl.generateToken(mockAuth);
        assertTrue(jwtTokenProviderImpl.validateToken(token));
    }

    @Test
    void shouldRetrieveUsernameFromValidToken() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProviderImpl.generateToken(mockAuth);
        Claims claims = jwtTokenProviderImpl.getClaimsFromToken(token);
        String retrieveUsername = claims.getSubject();

        assertEquals("testUser", retrieveUsername);
    }
    @Test
    void shouldRetrieveExpirationTimeFromToken() {
        String expectedUser = "testUser";

        when(mockAuth.getName()).thenReturn(expectedUser);

        String token = jwtTokenProviderImpl.generateToken(mockAuth);
        Claims claims = jwtTokenProviderImpl.getClaimsFromToken(token);

        assertNotNull(claims.getExpiration());
    }
    @Test
    void shouldThrowExceptionForExpiredToken() throws InterruptedException {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProviderImpl.generateToken(mockAuth);
        Thread.sleep(1001);
        assertThrows(TokenException.class, () -> jwtTokenProviderImpl.validateToken(token));
    }

    @Test
    void shouldThrowTokenExceptionForMalformedToken() {
        String malformedToken = "not-valid-token";

        assertThrows(TokenException.class, () -> jwtTokenProviderImpl.validateToken(malformedToken));
    }
    @Test
    void shouldThrowTokenExceptionForEmptyClaims() {
        String emptyClaimsToken = "";

        assertThrows(TokenException.class, () -> jwtTokenProviderImpl.validateToken(emptyClaimsToken));
    }
    @Test
    void shouldThrowTokenExceptionForInvalidAlgorithm() {
        String unsupportedToken = Jwts.builder()
                .setSubject("testUser")
                .setExpiration(new Date(System.currentTimeMillis() + 100L))
                .compact();

        assertThrows(TokenException.class, () -> jwtTokenProviderImpl.validateToken(unsupportedToken));
    }
}