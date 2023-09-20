package com.mycompany.SkySong.exception;

import com.mycompany.SkySong.location.service.LocationService;

public class LocationServiceException extends RuntimeException {
    public LocationServiceException(String message) {
        super(message);
    }
    public LocationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

