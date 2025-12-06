package com.mycompany.SkySong.identity.registration.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(@NotBlank(message = "The username field cannot be empty")
                                  @JsonProperty String username,
                                  @NotBlank(message = "The email field cannot be empty")
                                  @Email(message = "Invalid email address. Use the format name@domain (e.g., john.doe@example.com).")
                                  @JsonProperty String email,
                                  @NotBlank(message = "The password field cannot be empty")
                                  @JsonProperty String password) {}
