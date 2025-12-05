package com.mycompany.skysong.identity.application.authentication.model;

import java.time.Duration;
import java.time.Instant;

public record AccessToken(String value, Instant expiresAt) {
    public long expiresInSeconds(final Instant referenceTime) {
        return Duration.between(referenceTime, expiresAt).getSeconds();
    }
    @Override
    public String toString() {
        return "AccessToken(*****)";
    }
}