package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Clouds(@JsonProperty("all") Integer cloudCoverage) {
    public boolean isIncomplete() {
        return cloudCoverage == null || cloudCoverage < 0 || cloudCoverage > 100;
    }
}
