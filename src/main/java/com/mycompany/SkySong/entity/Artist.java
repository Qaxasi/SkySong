package com.mycompany.SkySong.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artist")
public class Artist {

    @Column(name = "artist_id")
    private Integer artistId;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "genres")
    private String[] genres;





}
