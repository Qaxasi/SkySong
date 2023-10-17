package com.mycompany.SkySong.authentication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotEmpty(message = "The usernameOrEmail field cannot be empty")
        String usernameOrEmail,
        @NotEmpty(message = "The password field cannot be empty")
        String password) {
}
