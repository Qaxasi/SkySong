package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "The username field cannot be empty")
        @JsonProperty String username,
        @NotEmpty(message = "The password field cannot be empty")
        @JsonProperty String password) {
}
