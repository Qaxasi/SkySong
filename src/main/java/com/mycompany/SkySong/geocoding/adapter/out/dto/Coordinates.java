package com.mycompany.SkySong.adapter.geocoding.dto;

public record Coordinates(double lat, double lon) {
    public boolean isValidCoordinate() {
        return lat() >= -90 && lat() <= 90 &&
                lon() >= -180 && lon() <= 180;
    }
}
