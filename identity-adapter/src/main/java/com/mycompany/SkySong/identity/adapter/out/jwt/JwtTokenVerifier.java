package com.mycompany.skysong.app.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenVerifier  {
    private final Key signKey;
    public JwtTokenVerifier(JwtCoreProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        this.signKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public Result<VerifiedJwt> verify(String jwt) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(jwt);

            return Result.success(new VerifiedJwt(jws.getBody()));

        } catch (ExpiredJwtException e) {
            return Result.failure("Access token expired", ErrorType.EXPIRED_JWT_TOKEN);
        } catch (IllegalArgumentException | JwtException e) {
            return Result.failure("Invalid token", ErrorType.INVALID_JWT_TOKEN);
        }
    }
}
