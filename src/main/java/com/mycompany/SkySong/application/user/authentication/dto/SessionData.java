package com.mycompany.SkySong.application.user.authentication.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record SessionData(
        int id,
        String usernameOrEmail,
        List<String> roles,
        Instant issueAt,
        Instant expiresAt) implements Serializable {
}
