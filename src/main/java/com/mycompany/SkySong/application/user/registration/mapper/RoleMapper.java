package com.mycompany.SkySong.application.user.registration.mapper;

import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.application.user.registration.dto.RoleDto;

public class RoleMapper {
    public Role toEntity(final RoleDto roleDTO) {
        return new Role(roleDTO.id(), roleDTO.name());
    }

    public RoleDto toDto(final Role role) {
        return new RoleDto(role.getId(), role.getName());
    }
}
