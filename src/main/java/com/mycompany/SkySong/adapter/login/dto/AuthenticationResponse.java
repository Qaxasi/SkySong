package com.mycompany.SkySong.adapter.login.dto;

public record AuthenticationResponse(String jwtToken, String refreshToken) {
}
