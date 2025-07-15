package com.mycompany.SkySong.geocoding.adapter.out.dto;

import java.util.List;

public record GeocodingApiResponse(List<ApiCoordinates> results) {
}
