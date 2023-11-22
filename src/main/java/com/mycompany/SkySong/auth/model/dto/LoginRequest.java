package com.mycompany.SkySong.auth.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "The usernameOrEmail field cannot be empty")
        String usernameOrEmail,
        @NotEmpty(message = "The password field cannot be empty")
        String password) {
}
