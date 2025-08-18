package com.mycompany.SkySong.identity.authentication.application.shared.dto;


import java.util.List;

public record AccessTokenClaims(int userId, String username, List<String> roles) {
}
