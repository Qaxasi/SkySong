package com.mycompany.SkySong.application.mappers;

import com.mycompany.SkySong.infrastructure.entity.Role;
import com.mycompany.SkySong.domain.dto.RoleDTO;

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
