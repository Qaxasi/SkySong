package com.mycompany.SkySong.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

public final class VerifiedJwt {
    private final Claims claims;

    public VerifiedJwt(final Claims claims) {
        this.claims = claims;
    }

    public Claims raw() {
        return claims;
    }

    public String username() {
        final Object u = claims.get("username");
        if (u == null) {
            return "";
        }
        final String s = u.toString().trim();
        return s.trim().isEmpty() ? "" : s;
    }

    public List<String> roles() {
        final Object v = claims.get("roles");
        if (v instanceof List<?> l) {
            return l.stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .toList();
        }
        return List.of();
    }

    public OptionalInt userId() {
        final String userId = claims.getSubject();
        if (userId != null && !userId.isBlank()) {
            try {
                return OptionalInt.of(Integer.parseInt(userId));
            } catch (NumberFormatException ignored) {}
        }
        return OptionalInt.empty();
    }

    public long sessionVersion() {
        final Object sessionVersion = claims.get("session_version");
        if (sessionVersion == null) {
            return 0L;
        }
        if (sessionVersion instanceof Number n) {
            return n.longValue();
        }
        try {
            return Long.parseLong(sessionVersion.toString());
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }
}
