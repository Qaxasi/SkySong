package com.mycompany.SkySong.authentication.model.dto;

import jakarta.validation.constraints.NotEmpty;
public record RegisterRequest(@NotEmpty(message = "The username field cannot be empty.")
                              String username,
                              @NotEmpty(message = "The email field cannot be empty")
                              String email,
                              @NotEmpty(message = "The password field cannot be empty")
                              String password) {
}
