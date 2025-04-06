package com.mycompany.SkySong.domain.geocoding.port;

import com.mycompany.SkySong.domain.geocoding.model.Location;

public interface GeocodingIntegration {
    Location fetchCoordinates(String address);
}
