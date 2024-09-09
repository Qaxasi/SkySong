package com.mycompany.SkySong.application.registration.dto;

import java.util.Set;

public record UserSaveDto(String username, String email, String password, Set<RoleDTO> roles) {
}
