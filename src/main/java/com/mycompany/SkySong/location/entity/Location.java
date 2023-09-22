package com.mycompany.SkySong.location.entity;

import com.mycompany.SkySong.user.entity.User;
import com.mycompany.SkySong.weather.entity.Weather;
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
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String state;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_id")
    private Weather weather;

    @OneToOne(mappedBy = "location")
    private User user;

}
