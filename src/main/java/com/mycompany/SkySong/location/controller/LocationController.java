package com.mycompany.SkySong.location.controller;

import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.location.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/location/coordinates/")
public class LocationController {
    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @GetMapping({ "", "{locationName}" })
    public LocationRequest fetchLocationCoordinates(
            @PathVariable(required = false) String locationName) throws IOException {
        if (locationName == null || locationName.isEmpty()) {
            throw new NullOrEmptyInputException(
                    "Location name cannot be null or empty. First you need to specify your location.");
        }
        return locationService.fetchAndSaveCoordinates(locationName);
    }
}
