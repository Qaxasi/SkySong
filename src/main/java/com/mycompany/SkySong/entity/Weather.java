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
@Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(name = "weather_id", nullable = false)
    private Integer weatherId;

    @Column(name = "weather", nullable = false)
    private String weather;

    @Column(name = "description", nullable = false)
    private String description;
}
