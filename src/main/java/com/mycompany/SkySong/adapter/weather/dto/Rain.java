package com.mycompany.SkySong.adapter.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Rain(@JsonProperty("1h") double rainVolume) {
}
