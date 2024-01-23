package com.mycompany.SkySong.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
public record RegisterRequest(@JsonProperty @NotEmpty(message = "The username field cannot be empty")
                              String username,
                              @JsonProperty @NotEmpty(message = "The email field cannot be empty")
                              String email,
                              @JsonProperty @NotEmpty(message = "The password field cannot be empty")
                              String password) {
}
