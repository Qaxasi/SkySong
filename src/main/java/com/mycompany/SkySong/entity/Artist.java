package com.mycompany.SkySong.entity;

import com.mycompany.SkySong.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "preferredArtist")
    private Set<User> usersWhoPreferThisArtist ;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
    private List<Track> tracks = new ArrayList<>();

}
