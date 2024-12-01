package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherType(@JsonProperty("main") String condition) {
}
