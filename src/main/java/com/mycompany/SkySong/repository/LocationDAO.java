package com.mycompany.SkySong.repository;

import com.mycompany.SkySong.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDAO extends JpaRepository<Location, Integer> {
    Location findByCityName(String cityName);

}
