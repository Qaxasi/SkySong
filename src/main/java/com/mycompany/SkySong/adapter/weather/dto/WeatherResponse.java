package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherResponse(@JsonProperty("main") AtmosphericConditionsDto conditionsDto,
                              @JsonProperty("clouds") CloudsInfoDto cloudsInfoDto,
                              @JsonProperty("wind") WindInfoDto windInfoDto,
                              @JsonProperty("sys") DaytimeInfoDto daytimeInfoDto,
                              @JsonProperty("weather") List<WeatherTypeDto> weather) {
}
