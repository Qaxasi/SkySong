package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloudsInfoDto(@JsonProperty("all") int cloudCoverage) {
}
