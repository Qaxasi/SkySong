package com.mycompany.SkySong.adapter.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "The usernameOrEmail field cannot be empty")
        @JsonProperty String usernameOrEmail,
        @NotEmpty(message = "The password field cannot be empty")
        @JsonProperty String password) {
}
