package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        String username,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull
        @Size(min = 1, max = 1024)
        char[] password) {

        @Override
        public String toString() {
                return String.format("******");
        }
}
