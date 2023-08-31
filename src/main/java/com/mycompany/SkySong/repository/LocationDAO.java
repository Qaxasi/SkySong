package com.mycompany.SkySong.repository;

import com.mycompany.SkySong.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDAO extends JpaRepository<Location, Integer> {
    Location findLocationByLocationName(String locationName);

}
