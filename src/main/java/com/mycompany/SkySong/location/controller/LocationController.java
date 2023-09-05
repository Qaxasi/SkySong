package com.mycompany.SkySong.location.LocationController;

import com.mycompany.SkySong.location.LocationEntity.LocationRequest;
import com.mycompany.SkySong.location.LocationService.LocationService;
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
