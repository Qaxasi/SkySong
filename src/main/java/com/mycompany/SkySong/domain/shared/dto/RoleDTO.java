package com.mycompany.SkySong.domain.dto;

import com.mycompany.SkySong.domain.shared.enums.UserRole;

public record RoleDTO(Integer id, UserRole name) {}