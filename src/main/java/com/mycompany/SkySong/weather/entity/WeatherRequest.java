package com.mycompany.SkySong.weather.WeatherEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.SkySong.weather.WeatherEntity.WeatherInfo;

import java.util.List;

public record WeatherRequest(@JsonProperty List<WeatherInfo> weather) {
}
