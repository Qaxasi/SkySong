package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AtmosphericConditions(@JsonProperty("temp") Double temperature,
                                    @JsonProperty("humidity") Integer humidity) {
    public boolean isIncomplete() {
        return temperature == null || temperature > 100 || temperature < -100 ||
                humidity == null || humidity > 100 || humidity < 0;
    }
}
