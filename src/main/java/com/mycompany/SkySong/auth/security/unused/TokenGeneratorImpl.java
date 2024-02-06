package com.mycompany.SkySong.auth.security.unused;

import com.mycompany.SkySong.auth.security.unused.DateProvider;
import com.mycompany.SkySong.auth.security.unused.KeyManager;
import com.mycompany.SkySong.auth.security.unused.TokenGenerator;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenGeneratorImpl implements TokenGenerator {
    private final long jwtExpirationDate;
    private final DateProvider dateProvider;
    private final KeyManager key;

    public TokenGeneratorImpl(@Value("${app-jwt-expiration-milliseconds}") long jwtExpirationDate,
                              DateProvider dateProvider,
                              KeyManager key) {
        this.jwtExpirationDate = jwtExpirationDate;
        this.dateProvider = dateProvider;
        this.key = key;
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
                .signWith(key.getKey())
                .compact();
    }
}
