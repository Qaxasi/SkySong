package com.mycompany.SkySong.dto;

import lombok.Data;

@Data
public class LocationDto {

    private Long id;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private String country;
    private String state;
    private Integer weatherId;

}
