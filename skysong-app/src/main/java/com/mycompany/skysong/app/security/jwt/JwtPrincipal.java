package com.mycompany.skysong.app.security.jwt;

import java.util.List;

public record JwtPrincipal(int userId, String username, List<String> roles, long sessionVersion) {
}
