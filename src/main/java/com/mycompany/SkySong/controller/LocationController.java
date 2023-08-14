package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location/coordinates/")
public class LocationController {
    private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("{cityName}")
    public ResponseEntity<Location> getLocationCoordinates(@PathVariable(name = "cityName") String cityName) {
        Location searchedLocation = locationService.getLocationCoordinatesByCityName(cityName);
        return new ResponseEntity<>(searchedLocation, HttpStatus.OK);
    }
}
