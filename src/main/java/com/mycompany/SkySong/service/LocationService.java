package com.mycompany.SkySong.service;

import com.mycompany.SkySong.entity.LocationRequest;

import java.io.IOException;

public interface LocationService {
    LocationRequest fetchAndSaveCoordinates(String locationName) throws IOException;
}
