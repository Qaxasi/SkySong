package com.mycompany.SkySong.identity.authentication.application.login.dto;

import java.util.Set;

public record AuthenticatedUser(int id, String username, Set<String> roles) {
}
