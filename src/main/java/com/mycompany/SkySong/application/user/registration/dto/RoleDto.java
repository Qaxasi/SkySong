package com.mycompany.SkySong.application.user.registration.dto;

import com.mycompany.SkySong.domain.shared.enums.UserRole;

public record RoleDto(Integer id, UserRole name) {}