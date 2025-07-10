package com.mycompany.SkySong.weather.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AtmosphericConditions(@JsonProperty("temp") Double temperature,
                                    @JsonProperty("humidity") Integer humidity) {
}
