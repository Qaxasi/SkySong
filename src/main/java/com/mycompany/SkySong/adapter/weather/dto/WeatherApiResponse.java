package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherApiResponse(@JsonProperty("main") AtmosphericConditions atmosphericConditions,
                                 @JsonProperty("clouds") Clouds clouds,
                                 @JsonProperty("wind") Wind wind,
                                 @JsonProperty("sys") Daytime daytime,
                                 @JsonProperty("weather") List<WeatherType> conditions,
                                 @JsonProperty("rain") Rain rain,
                                 @JsonProperty("snow") Snow snow) {

    public boolean isIncomplete() {
        return daytime == null || daytime().isIncomplete()
                || wind == null || wind().isIncomplete()
                || clouds == null || clouds.isIncomplete()
                || atmosphericConditions == null || atmosphericConditions.isIncomplete()
                || conditions == null || conditions.stream().anyMatch(WeatherType::isIncomplete);

    }
}
