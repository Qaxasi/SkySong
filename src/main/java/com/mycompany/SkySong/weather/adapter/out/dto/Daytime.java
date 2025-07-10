package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Daytime(@JsonProperty("sunrise") Integer sunrise,
                      @JsonProperty("sunset") Integer sunset) {
}
