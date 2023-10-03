package com.mycompany.SkySong.authentication.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull String usernameOrEmail,
        @NotNull String password) {
}
