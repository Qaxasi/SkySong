package com.mycompany.SkySong.common.mapper;

import com.mycompany.SkySong.common.entity.Role;
import com.mycompany.SkySong.registration.dto.RoleDTO;

public class RoleMapper {
    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.id());
        role.setName(roleDTO.name());
        return role;
    }
    public RoleDTO toDto(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }
}
