package com.mycompany.SkySong.application.registration.dto;

import com.mycompany.SkySong.domain.shared.enums.UserRole;

public record RoleDTO(Integer id, UserRole name) {}