package com.mycompany.SkySong.authentication.dto;

public record JWTAuthResponse(String accessToken, String tokenType) {
    public JWTAuthResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}
