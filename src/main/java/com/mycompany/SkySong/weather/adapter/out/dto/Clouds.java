package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Clouds(@JsonProperty("all") Integer cloudCoverage) {
}
