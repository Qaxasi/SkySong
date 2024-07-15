package com.mycompany.SkySong.domain.registration.dto;

import com.mycompany.SkySong.domain.shared.dto.RoleDTO;

import java.util.Set;

public record UserRegistrationDTO(String username, String email, String password, Set<RoleDTO> roles) {
}
