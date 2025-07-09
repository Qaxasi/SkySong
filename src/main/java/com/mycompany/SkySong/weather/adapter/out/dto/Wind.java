package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Wind(@JsonProperty("speed") Double speed) {
    public boolean isIncomplete() {
        return speed == null || speed < 0;
    }
}
