package com.mycompany.SkySong.registration.dto;

import java.util.Set;

public record UserRegistrationDTO(String username, String email, String password, Set<RoleDTO> roles) {
}
