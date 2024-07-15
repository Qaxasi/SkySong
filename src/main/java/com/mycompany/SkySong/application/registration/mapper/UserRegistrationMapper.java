package com.mycompany.SkySong.application.mapper;

import com.mycompany.SkySong.application.shared.RoleMapper;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.application.registration.dto.UserRegistrationDTO;

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
