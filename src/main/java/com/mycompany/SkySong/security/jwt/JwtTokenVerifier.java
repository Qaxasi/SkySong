package com.mycompany.SkySong.adapter.security.jwt;

import com.mycompany.SkySong.adapter.exception.security.ExpiredTokenException;
import com.mycompany.SkySong.adapter.exception.security.InvalidTokenException;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtTokenVerifier  {
    private final Key signKey;
    private final ApplicationLogger logger;

    public JwtTokenVerifier(@Value("${application.security.jwt.secret-key}") String secretKey,
                            ApplicationLogger logger) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signKey = Keys.hmacShaKeyFor(keyBytes);
        this.logger = logger;
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expired",
                    ApplicationLogger.Context.of("message", e.getMessage()));
            throw new ExpiredTokenException("Jwt token has expired.");

        } catch (MalformedJwtException e) {
            logger.warn("JWT token is malformed",
                    ApplicationLogger.Context.of("message", e.getMessage()));
            throw new InvalidTokenException("Jwt token is malformed.");

        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token uses unsupported format or algorithm",
                    ApplicationLogger.Context.of("message", e.getMessage()));
            throw new InvalidTokenException("Jwt token uses unsupported format or algorithm.");

        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("JWT token is invalid",
                    ApplicationLogger.Context.of("message", e.getMessage()));
            throw new InvalidTokenException("JWT token is invalid.");
        }
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims parseClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
