package com.mycompany.SkySong.identity.authentication.application.shared.dto;

import java.time.Duration;
import java.time.Instant;

public record AccessToken(String value, Instant expiresAt) {
    public Duration remainingTtl(final Instant referenceTime) {
        return Duration.between(referenceTime, expiresAt);
    }
    @Override
    public String toString() {
        return "AccessToken(*****)";
    }
}
