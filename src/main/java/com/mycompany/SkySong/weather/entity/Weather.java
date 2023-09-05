package com.mycompany.SkySong.weather.entity;

import com.mycompany.SkySong.location.entity.Location;
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

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(name = "weather", nullable = false)
    private String weather;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToOne(mappedBy = "weather",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location location;

}
