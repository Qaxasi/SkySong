package com.mycompany.SkySong.adapter.geocoding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationResponse(
        @JsonProperty("lat") Double latitude,
        @JsonProperty("lon") Double longitude) {
}
