package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherApiResponse(@JsonProperty("main") AtmosphericConditions atmosphericConditions,
                                 @JsonProperty("clouds") Clouds clouds,
                                 @JsonProperty("wind") Wind wind,
                                 @JsonProperty("sys") Daytime daytime,
                                 @JsonProperty("rain") Rain rain,
                                 @JsonProperty("snow") Snow snow) {

    public boolean isIncomplete() {
        return daytime == null || daytime().isIncomplete()
                || wind == null || wind().isIncomplete()
                || clouds == null || clouds.isIncomplete()
                || atmosphericConditions == null || atmosphericConditions.isIncomplete();

    }
}
