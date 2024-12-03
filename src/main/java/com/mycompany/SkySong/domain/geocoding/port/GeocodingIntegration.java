package com.mycompany.SkySong.domain.geocoding.port;

import com.mycompany.SkySong.domain.geocoding.model.Location;
import com.mycompany.SkySong.shared.utils.Result;

public interface GeocodingIntegration {
    Result<Location> getCoordinates(String address);
}
