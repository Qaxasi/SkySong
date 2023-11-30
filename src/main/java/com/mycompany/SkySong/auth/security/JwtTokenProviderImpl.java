package com.mycompany.SkySong.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
class JwtTokenProviderImpl implements JwtTokenProvider {
    private final String jwtSecret;
    private final long jwtExpirationDate;
    private final JwtExceptionHandler jwtExceptionHandler;
    private final DateProvider dateProvider;
    public JwtTokenProviderImpl(@Value("${JWT_SECRET}") String jwtSecret,
                                @Value("${app-jwt-expiration-milliseconds}") long jwtExpirationDate,
                                JwtExceptionHandler jwtExceptionHandler,
                                DateProvider dateProvider) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationDate = jwtExpirationDate;
        this.jwtExceptionHandler = jwtExceptionHandler;
        this.dateProvider = dateProvider;
    }
    @Override
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = dateProvider.getCurrentDate();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }
    @Override
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject() != null && !claims.getSubject().isEmpty();
        } catch (Exception e) {
            jwtExceptionHandler.handleException(e);
            return false;
        }
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
