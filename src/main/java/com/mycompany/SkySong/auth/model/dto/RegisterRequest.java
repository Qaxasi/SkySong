package com.mycompany.SkySong.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
public record RegisterRequest(@NotEmpty(message = "The username field cannot be empty")
                              @JsonProperty String username,
                              @NotEmpty(message = "The email field cannot be empty")
                              @JsonProperty String email,
                              @NotEmpty(message = "The password field cannot be empty")
                              @JsonProperty String password) {
}
