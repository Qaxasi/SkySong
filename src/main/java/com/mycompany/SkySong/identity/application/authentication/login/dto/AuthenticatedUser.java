package com.mycompany.SkySong.identity.application.authentication.login.dto;

import com.mycompany.SkySong.identity.application.authentication.shared.dto.AccessTokenPayload;

import java.util.List;

public record AuthenticatedUser(int id, String username, List<String> roles) implements AccessTokenPayload {
}
