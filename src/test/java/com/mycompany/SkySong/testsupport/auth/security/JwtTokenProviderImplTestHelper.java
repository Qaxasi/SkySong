package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.DateProvider;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.auth.security.JwtTokenProviderImplTest;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.Mockito.when;

public class JwtTokenProviderImplTestHelper {
    private static final long EXPIRATION_MS = 1000L;
    private static Date getCurrentDate() {
        return new Date();
    }
    private static Date getExpirationDate(boolean expired) {
        Date now = getCurrentDate();
        return new Date(now.getTime() + (expired ? -EXPIRATION_MS : EXPIRATION_MS));
    }
    private static JwtBuilder createBaseBuilder(Date now, Date expirationDate, Key key) {
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key);
    }
    public static String generateMalformedToken(Key key) {
        String token = createBaseBuilder(getCurrentDate(), getExpirationDate(false), key)
                .setSubject("testUser")
                .compact();
        return token.substring(0, token.length() / 2);
    }
    public static String generateTokenWithUnsupportedSignature(Key key) {
        Key strongerKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return createBaseBuilder(getCurrentDate(), getExpirationDate(false), key)
                .setSubject("testUser")
                .signWith(strongerKey, SignatureAlgorithm.HS512)
                .compact();
    }
    public static String generateTokenWithEmptyClaims(Key key) {
        return createBaseBuilder(getCurrentDate(), getExpirationDate(false), key)
                .setClaims(new HashMap<>())
                .compact();
    }
    public static String generateExpiredToken(Key key) {
        return createBaseBuilder(getCurrentDate(), getExpirationDate(true), key)
                .setSubject("testUser")
                .compact();
    }
    public static String generateTokenWithInvalidSignature(Key key) {
        String token = createBaseBuilder(getCurrentDate(), getExpirationDate(false), key)
                .setSubject("testUser")
                .compact();
        return token + "invalid";
    }
    public static String generateValidToken(Authentication auth,
                                            JwtTokenProvider tokenProvider,
                                            DateProvider dateProvider) {
        when(auth.getName()).thenReturn("user");
        when(dateProvider.getCurrentDate()).thenReturn(getCurrentDate());
        return tokenProvider.generateToken(auth);
    }
}
