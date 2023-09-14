package com.mycompany.SkySong.config;

import com.fasterxml.jackson.annotation.JsonProperty;
public record ErrorResponse(
        @JsonProperty("Error message") String message
) {
}
