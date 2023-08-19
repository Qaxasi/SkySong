package com.mycompany.SkySong.service;

import com.mycompany.SkySong.dto.LocationDto;
public interface LocationService {
    LocationDto getLocationCoordinatesByLocationName(String locationName);
}
