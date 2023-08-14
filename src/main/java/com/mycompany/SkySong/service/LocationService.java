package com.mycompany.SkySong.service;

import com.mycompany.SkySong.entity.Location;

public interface LocationService {
    Location getLocationCoordinatesByCityName(String cityName);
}
