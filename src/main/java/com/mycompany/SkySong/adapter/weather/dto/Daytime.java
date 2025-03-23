package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Daytime(@JsonProperty("sunrise") int sunrise,
                      @JsonProperty("sunset") int sunset) {
    public boolean isIncomplete() {
        return sunrise == 0 || sunset == 0;
    }
}
