package com.mycompany.SkySong.authentication.secutiry;

import com.mycompany.SkySong.authentication.exception.TokenException;
import com.mycompany.SkySong.authentication.secutiry.service.interfaces.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.impl.ApplicationMessageService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final String jwtSecret;
    private final long jwtExpirationDate;
    private final ApplicationMessageService messageService;
    private final JwtExceptionHandler jwtExceptionHandler;
    public JwtTokenProviderImpl(@Value("${JWT_SECRET}") String jwtSecret,
                                @Value("${app-jwt-expiration-milliseconds}") long jwtExpirationDate,
                                ApplicationMessageService messageService, JwtExceptionHandler jwtExceptionHandler) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationDate = jwtExpirationDate;
        this.messageService = messageService;
        this.jwtExceptionHandler = jwtExceptionHandler;
    }
    @Override
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
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
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            jwtExceptionHandler.handleException(e);
            return false;
        }
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
