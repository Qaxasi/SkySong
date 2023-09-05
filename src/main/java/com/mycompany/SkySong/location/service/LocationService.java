package com.mycompany.SkySong.location.service;

import com.mycompany.SkySong.location.entity.LocationRequest;

import java.io.IOException;

public interface LocationService {
    LocationRequest fetchAndSaveCoordinates(String locationName) throws IOException;
}
