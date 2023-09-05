package com.mycompany.SkySong.location.LocationService;

import com.mycompany.SkySong.location.LocationEntity.LocationRequest;

import java.io.IOException;

public interface LocationService {
    LocationRequest fetchAndSaveCoordinates(String locationName) throws IOException;
}
