package com.mycompany.SkySong.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "track")
public class Track {

    private Integer id;
    private String title;
    private String artist;
    private String album;
    private Integer duration;
    private Date release_date;
    private String genre;
    private String mood;















}
