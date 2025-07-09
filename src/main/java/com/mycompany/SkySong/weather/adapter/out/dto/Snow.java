package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Snow(@JsonProperty("1h") double snowVolume) {
}
