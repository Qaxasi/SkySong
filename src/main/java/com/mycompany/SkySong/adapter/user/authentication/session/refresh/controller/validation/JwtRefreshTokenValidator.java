package com.mycompany.SkySong.adapter.user.token.refresh.validation;

import com.mycompany.SkySong.application.user.authentication.session.refresh.exception.ExpiredRefreshTokenException;
import com.mycompany.SkySong.application.user.authentication.session.refresh.exception.InvalidRefreshTokenException;
import com.mycompany.SkySong.application.user.authentication.session.refresh.ports.RefreshTokenValidator;
import com.mycompany.SkySong.shared.config.security.jwt.JwtProperties;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
class JwtRefreshTokenValidator implements RefreshTokenValidator {

    private final SecretKey signKey;
    private final ApplicationLogger logger;

    JwtRefreshTokenValidator(final JwtProperties jwtProperties,
                             final ApplicationLogger logger) {
        this.signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
        this.logger = logger;
    }

    @Override
    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            logger.warn("Refresh token expired", context("message", e.getMessage()));
            throw new ExpiredRefreshTokenException("Refresh token has expired");

        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Refresh token invalid", context("message", e.getMessage()));
            throw new InvalidRefreshTokenException("Refresh token is invalid");
        }
    }
}
