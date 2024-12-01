package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DaytimeData(@JsonProperty("sunrise") int sunrise,
                          @JsonProperty("sunset") int sunset) {
}
