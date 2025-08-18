package com.mycompany.SkySong.identity.authentication.application.login.dto;

import java.util.List;

public record AuthenticatedUser(int id, String username, List<String> roles) {
}
