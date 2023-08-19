package com.mycompany.SkySong.dto;

import lombok.Data;

@Data
public class WeatherDto {

    private Integer id;
    private String locationName;
    private Integer weatherId;
    private String weather;
    private String description;
    private Integer locationId;
}
