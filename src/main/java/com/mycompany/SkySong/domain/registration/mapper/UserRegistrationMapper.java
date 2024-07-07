package com.mycompany.SkySong.application.mappers;

import com.mycompany.SkySong.application.mappers.RoleMapper;
import com.mycompany.SkySong.infrastructure.entity.User;
import com.mycompany.SkySong.domain.registration.dto.UserRegistrationDTO;

import java.util.stream.Collectors;

public class UserRegistrationMapper {
    private final RoleMapper roleMapper;
    public UserRegistrationMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public User toEntity(UserRegistrationDTO userDto) {
        User user = new User();
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setRoles(userDto.roles().stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet()));
        return user;
    }
    public UserRegistrationDTO toDto(User user) {
        return new UserRegistrationDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(roleMapper::toDto).collect(Collectors.toSet()));
    }
 }
