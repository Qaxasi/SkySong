package com.mycompany.SkySong.dto;

import lombok.Data;

@Data
public class TrackDto {

    private Integer id;
    private String spotifyTrackId;
    private String title;
    private Integer duration;
    private String url;
    private Integer artistId;
    private Integer albumId;


}
