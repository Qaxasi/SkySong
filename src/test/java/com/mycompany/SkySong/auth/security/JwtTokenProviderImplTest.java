package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.Claims;
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

import static com.mycompany.SkySong.testsupport.auth.security.JwtTokenProviderImplTestHelper.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderImplTest {
    private JwtTokenProviderImpl tokenProvider;
    @Mock
    private Authentication mockAuth;
    @Mock
    private JwtExceptionHandler handler;
    @Mock
    private DateProvider dateProvider;
    private Key key;
    @BeforeEach
    public void setUp() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());
        long jwtExpirationTime = 1000L;

        tokenProvider = new JwtTokenProviderImpl(base64Secret, jwtExpirationTime, handler, dateProvider);
    }
    @Test
    void whenGivenAuth_GenerateToken() {
        assertNotNull(generateValidToken(mockAuth, tokenProvider, dateProvider));
    }
    @Test
    void whenGivenAuth_ValidateToken() {
        assertTrue(tokenProvider.validateToken(generateValidToken(mockAuth, tokenProvider, dateProvider)));
    }
    @Test
    void shouldRetrieveUsernameFromValidToken() {
        when(mockAuth.getName()).thenReturn("testUser");

        when(dateProvider.getCurrentDate()).thenReturn(new Date());

        String token = tokenProvider.generateToken(mockAuth);
        Claims claims = tokenProvider.getClaimsFromToken(token);
        String retrieveUsername = claims.getSubject();

        assertEquals("testUser", retrieveUsername);
    }
    @Test
    void shouldRetrieveExpirationTimeFromToken() {
        when(mockAuth.getName()).thenReturn("testUser");

        when(dateProvider.getCurrentDate()).thenReturn(new Date());

        String token = tokenProvider.generateToken(mockAuth);
        Claims claims = tokenProvider.getClaimsFromToken(token);

        assertNotNull(claims.getExpiration());
    }
    @Test
    void whenExpiredToken_ValidationFalse() {
        assertFalse(tokenProvider.validateToken(generateExpiredToken(key)));
    }
    @Test
    void whenMalformedToken_ValidationFalse() {
        assertFalse(tokenProvider.validateToken(generateMalformedToken(key)));
    }
    @Test
    void whenTokenHasUnsupportedSignature_ValidationFalse() {
        assertFalse(tokenProvider.validateToken(generateTokenWithUnsupportedSignature(key)));
    }
    @Test
    void whenTokenHasInvalidSignature_ValidationFalse() {
        assertFalse(tokenProvider.validateToken(generateTokenWithInvalidSignature(key)));
    }
    @Test
    void whenTokenHasEmptyClaims_ValidationFalse() {
        assertFalse(tokenProvider.validateToken(generateTokenWithEmptyClaims(key)));
    }
}