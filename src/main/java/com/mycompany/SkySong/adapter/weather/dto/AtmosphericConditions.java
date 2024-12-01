package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AtmosphericConditionsData(@JsonProperty("temp") double temperature,
                                        @JsonProperty("humidity") int humidity) {
}
