package com.mycompany.SkySong.authentication.model.dto;

public record JWTAuthResponse(String accessToken, String tokenType) {
    public JWTAuthResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}
