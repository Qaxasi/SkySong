package com.mycompany.SkySong.weather.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherRequest(@JsonProperty List<WeatherInfo> weather) {
}
