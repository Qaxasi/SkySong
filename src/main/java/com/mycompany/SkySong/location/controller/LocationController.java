package com.mycompany.SkySong.location.controller;

import com.mycompany.SkySong.config.ErrorResponse;
import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.location.exception.LocationNotGiven;
import com.mycompany.SkySong.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/location/coordinates/")
public class LocationController {
    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public void handleErrorWhenLocationIsNotGiven() {
        throw new LocationNotGiven();
    }
    @GetMapping("{locationName}")
    public LocationRequest fetchLocationCoordinates(
            @PathVariable String locationName) throws IOException {
        return locationService.fetchAndSaveCoordinates(locationName);
    }
}
