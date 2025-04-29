package com.mycompany.SkySong.application.user.registration.dto;

import java.util.Set;

public record UserSaveDto(String username, String email, String password, Set<RoleDto> roles) {
}
