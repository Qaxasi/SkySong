package com.mycompany.SkySong.identity.authentication.shared.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record SessionData(
        int userId,
        String username,
        List<String> roles,
        Instant issueAt,
        Instant expiresAt) implements Serializable, AccessTokenPayload {
}
