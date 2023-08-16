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

    @GetMapping("{localityName}")
    public ResponseEntity<Location> getLocationCoordinates(@PathVariable(name = "localityName") String localityName) {
        Location searchedLocation = locationService.getLocationCoordinatesByLocalityName(localityName);
        return new ResponseEntity<>(searchedLocation, HttpStatus.OK);
    }
}
