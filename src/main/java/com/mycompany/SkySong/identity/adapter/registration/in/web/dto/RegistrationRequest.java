package com.mycompany.SkySong.identity.adapter.registration.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record RegistrationRequest(@NotEmpty(message = "The username field cannot be empty")
                                  @JsonProperty String username,
                                  @NotEmpty(message = "The email field cannot be empty")
                                  @JsonProperty String email,
                                  @NotEmpty(message = "The password field cannot be empty")
                                  @JsonProperty String password) {

}
