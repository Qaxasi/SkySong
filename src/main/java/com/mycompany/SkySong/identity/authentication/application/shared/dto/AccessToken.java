package com.mycompany.SkySong.identity.authentication.application.shared.dto;

import java.time.Instant;

public record AccessToken(String value, Instant expiresAt) {
    @Override
    public String toString() {
        return "AccessToken(*****)";
    }
}
