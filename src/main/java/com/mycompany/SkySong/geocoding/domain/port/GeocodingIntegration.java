package com.mycompany.SkySong.geocoding.domain.port;

import com.mycompany.SkySong.geocoding.domain.model.Coordinates;

public interface GeocodingIntegration {
    Coordinates fetchCoordinates(String address);
}
