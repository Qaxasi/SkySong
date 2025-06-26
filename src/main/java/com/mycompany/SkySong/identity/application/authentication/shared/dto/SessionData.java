package com.mycompany.SkySong.identity.application.authentication.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record SessionData(
        int id,
        String username,
        List<String> roles,
        Instant issueAt,
        Instant expiresAt) implements Serializable, AccessTokenPayload {
}
