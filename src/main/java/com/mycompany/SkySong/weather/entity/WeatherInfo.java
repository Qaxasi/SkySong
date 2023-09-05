package com.mycompany.SkySong.weather.WeatherEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherInfo(@JsonProperty("main") String weather,
                          @JsonProperty("description") String description) {
}
