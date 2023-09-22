package com.mycompany.SkySong.entity;

import com.mycompany.SkySong.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genres")
public class Genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "preferredGenres")
    private Set<User> usersWhoPrefer = new HashSet<>();

    @ManyToMany(mappedBy = "dislikedGenres")
    private Set<User> usersWhoDislike;

}
