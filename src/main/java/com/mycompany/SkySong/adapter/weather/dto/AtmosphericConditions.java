package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AtmosphericConditions(@JsonProperty("temp") double temperature,
                                    @JsonProperty("humidity") int humidity) {
}
