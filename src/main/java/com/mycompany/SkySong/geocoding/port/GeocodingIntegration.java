package com.mycompany.SkySong.geocoding.port;

import com.mycompany.SkySong.geocoding.model.Coordinates;

public interface GeocodingIntegration {
    Coordinates fetchCoordinates(String address);
}
