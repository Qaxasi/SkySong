package com.mycompany.SkySong.application.user.authentication.session.refresh.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record SessionData(
        int id,
        String usernameOrEmail,
        List<String> roles,
        String refreshToken,
        Instant issueAt,
        Instant expiresAt) implements Serializable {
}
