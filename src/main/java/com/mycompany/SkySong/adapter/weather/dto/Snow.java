package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Snow(@JsonProperty("1h") double snowVolume) {
}
