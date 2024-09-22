package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherResponse(@JsonProperty("main") AtmosphericConditionsDto atmosphericConditions,
                              @JsonProperty("clouds") CloudsInfoDto cloudsInfo,
                              @JsonProperty("wind") WindInfoDto windInfo,
                              @JsonProperty("sys") DaytimeInfoDto daytimeInfo,
                              @JsonProperty("weather") List<WeatherTypeDto> weather) {
}
