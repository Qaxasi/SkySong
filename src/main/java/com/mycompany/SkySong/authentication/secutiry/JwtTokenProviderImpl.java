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
    public JwtTokenProviderImpl(@Value("${JWT_SECRET}") String jwtSecret,
                                @Value("${app-jwt-expiration-milliseconds}") long jwtExpirationDate,
                                ApplicationMessageService messageService) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationDate = jwtExpirationDate;
        this.messageService = messageService;
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
        } catch (ExpiredJwtException e) {
            throw new TokenException(messageService.getMessage("jwt.expired"));
        } catch (MalformedJwtException e) {
            throw new TokenException(messageService.getMessage("jwt.invalid"));
        } catch (UnsupportedJwtException e) {
            throw new TokenException(messageService.getMessage("jwt.unsupported"));
        } catch (IllegalArgumentException e) {
            throw new TokenException(messageService.getMessage("jwt.claims.empty"));
        }
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
