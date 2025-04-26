package com.mycompany.SkySong.application.registration.mapper;

import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.application.registration.dto.RoleDTO;

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
