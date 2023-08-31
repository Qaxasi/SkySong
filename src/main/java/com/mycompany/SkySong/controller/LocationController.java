package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.entity.LocationRequest;
import com.mycompany.SkySong.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/location/coordinates/")
public class LocationController {
    @Autowired
    private LocationService locationService;
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("{locationName}")
    public LocationRequest getLocationCoordinates(
            @PathVariable String locationName) throws IOException {
        return locationService.fetchAndSaveCoordinates(locationName);

    }
}
