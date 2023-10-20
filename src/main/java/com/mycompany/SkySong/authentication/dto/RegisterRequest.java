package com.mycompany.SkySong.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@NotEmpty(message = "The username field cannot be empty.")
                              String username,
                              @Email(message = "Invalid email address format. The email should follow the standard" +
                                      " format (e.g., user@example.com).")
                              @NotEmpty(message = "The email field cannot be empty")
                              String email,
                              @NotEmpty(message = "The password field cannot be empty")
                              String password) {
}
