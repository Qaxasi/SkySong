package com.mycompany.skysong.identity.adapter.out.security.jwt;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

@Component
public class JwtTokenVerifier  {
    private final Key signKey;
    public JwtTokenVerifier(final SecretKey signKey){
        this.signKey = signKey;
    }

    public Result<VerifiedJwt> verify(final String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            return Result.failure("Missing token", ErrorType.INVALID_JWT_TOKEN);
        }

        try {
            final Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(jwt);

            return VerifiedJwt.fromClaims(claimsJws.getBody());

        } catch (ExpiredJwtException e) {
            return Result.failure("Expired token", ErrorType.EXPIRED_JWT_TOKEN);
        } catch (IllegalArgumentException | JwtException e) {
            return Result.failure("Invalid token", ErrorType.INVALID_JWT_TOKEN);
        }
    }
}
