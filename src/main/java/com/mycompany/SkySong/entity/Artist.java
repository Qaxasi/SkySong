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
@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "spotify_artist_id")
    private String spotifyArtistId;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "genres")
    private String genres;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Track Track;

}
