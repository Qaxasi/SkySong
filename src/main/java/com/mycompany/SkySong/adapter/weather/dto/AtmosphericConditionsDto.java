package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AtmosphericConditionsDto(@JsonProperty("temp") double temperature,
                                       @JsonProperty("humidity") int humidity) {
}
