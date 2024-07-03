package com.mycompany.SkySong.common.mapper;

import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.common.mapper.RoleMapper;
import com.mycompany.SkySong.registration.dto.UserDTO;

import java.util.stream.Collectors;

public class UserMapper {
    private final RoleMapper roleMapper;
    UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setRoles(userDTO.roles().stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet()));
        return user;
    }
    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(roleMapper::toDto)
                        .collect(Collectors.toSet()));
    }
}
