package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "The username field cannot be blank")
        String username,
        @NotBlank(message = "The password field cannot be blank")
        String password) {
}
