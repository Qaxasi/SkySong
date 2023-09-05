package com.mycompany.SkySong.location.LocationRepository;

import com.mycompany.SkySong.location.LocationEntity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDAO extends JpaRepository<Location, Integer> {
    Location findLocationByLocationName(String locationName);

}
