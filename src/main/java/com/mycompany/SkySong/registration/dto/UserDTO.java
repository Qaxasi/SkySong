package com.mycompany.SkySong.registration.dto;

import java.util.Set;

public record UserDTO(Integer id, String username, String email, Set<RoleDTO> roles) {}