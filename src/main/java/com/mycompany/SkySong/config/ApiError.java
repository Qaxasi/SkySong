package com.mycompany.SkySong.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiError(
        @JsonProperty("Error message") String message
) {
}
