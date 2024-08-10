package com.mycompany.SkySong.application.registration.mapper;

import com.mycompany.SkySong.application.registration.dto.RoleDTO;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleMapperTest {

    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapper();
    }

    @Test
    void whenMappingRoleDtoToEntity_FieldsAreCorrectlySet() {
        RoleDTO roleDTO = new RoleDTO(1, UserRole.ROLE_USER);

        Role role = toEntity(roleDTO);

        assertEquals(1, role.getId());
        assertEquals(UserRole.ROLE_USER, role.getName());
    }

    @Test
    void whenMappingRoleEntityToDto_FieldsAreCorrectlySet() {
        Role role = new Role(1, UserRole.ROLE_USER);

        RoleDTO roleDTO = toDto(role);

        assertEquals(1, roleDTO.id());
        assertEquals(UserRole.ROLE_USER, roleDTO.name());
    }

    private Role toEntity(RoleDTO roleDTO) {
        return roleMapper.toEntity(roleDTO);
    }

    private RoleDTO toDto(Role role) {
        return roleMapper.toDto(role);
    }
}
