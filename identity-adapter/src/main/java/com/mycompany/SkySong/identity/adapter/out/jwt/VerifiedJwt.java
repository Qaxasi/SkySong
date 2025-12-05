package com.mycompany.SkySong.identity.adapter.out.jwt;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Objects;

public final class VerifiedJwt {
    private final Claims claims;
    private final int userId;
    private final String username;
    private final List<String> roles;

    public VerifiedJwt(final Claims claims,
                       final int userId,
                       final String username,
                       final List<String> roles) {
        this.claims = claims;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    public static Result<VerifiedJwt> fromClaims(final Claims claims) {
        if (claims == null) {
            return Result.failure("Missing claims in jwt token", ErrorType.INVALID_JWT_TOKEN);
        }

        return Result.combineM(
                extractUserId(claims),
                extractUsername(claims),

                (userId, username) -> {
                    final List<String> roles = extractRoles(claims);
                    return Result.success(new VerifiedJwt(
                            claims,
                            userId,
                            username,
                            List.copyOf(roles)));
                });
    }

    private static Result<String> extractUsername(final Claims claims) {
        final Object rawUsername = claims.get("username");
        if (rawUsername == null) {
            return Result.failure("Missing username in jwt token", ErrorType.INVALID_JWT_TOKEN);
        }
        final String username = rawUsername.toString().trim();
        if (username.isEmpty()) {
            return Result.failure("Invalid username in jwt token", ErrorType.INVALID_JWT_TOKEN);
        }
        return Result.success(username);
    }

    private static Result<Integer> extractUserId(final Claims claims) {
        final String subject = claims.getSubject();
        if (subject == null || subject.isBlank()) {
            return Result.failure("Missing subject in jwt token", ErrorType.INVALID_JWT_TOKEN);
        }

        final int userId;

        try {
            userId = Integer.parseInt(subject);
            if (userId < 1) {
                return Result.failure("Invalid subject in jwt token", ErrorType.INVALID_JWT_TOKEN);
            }
        } catch (NumberFormatException ex) {
            return Result.failure("Invalid subject in jwt token", ErrorType.INVALID_JWT_TOKEN);
        }
        return Result.success(userId);
    }

    private static List<String> extractRoles(final Claims claims) {
        final Object o = claims.get("roles");
        if (!(o instanceof List<?> list)) {
            return List.of();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();
    }

    public Claims raw() {
        return claims;
    }

    public String username() {
        return username;
    }

    public int userId() {
        return userId;
    }

    public List<String> roles() {
        return roles;
    }
}
