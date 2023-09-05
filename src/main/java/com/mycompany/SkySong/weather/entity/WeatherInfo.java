package com.mycompany.SkySong.weather.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherInfo(@JsonProperty("main") String weather,
                          @JsonProperty("description") String description) {
}
