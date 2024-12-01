package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherResponse(@JsonProperty("main") AtmosphericConditionsData atmosphericConditions,
                              @JsonProperty("clouds") CloudsData clouds,
                              @JsonProperty("wind") WindData wind,
                              @JsonProperty("sys") DaytimeData daytime,
                              @JsonProperty("weather") List<WeatherTypeData> conditions) {
}
