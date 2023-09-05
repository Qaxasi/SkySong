package com.mycompany.SkySong.location.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationRequest(@JsonProperty("name") String locationName,
                              @JsonProperty("lat") Double latitude,
                              @JsonProperty("lon") Double longitude,
                              @JsonProperty("country") String country,
                              @JsonProperty("state") String state)
{
}
