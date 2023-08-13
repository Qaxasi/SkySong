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
@Table(name = "location", uniqueConstraints = @UniqueConstraint(columnNames = "cityName"))
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column
    private int id;

    @Column
    String cityName;

    @Column
    double latitude;

    @Column
    double longitude;




}
