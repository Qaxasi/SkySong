package com.mycompany.SkySong.application.registration.mapper;

import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.application.registration.dto.UserSaveDto;

import java.util.stream.Collectors;

public class UserRegistrationMapper {
    private final RoleMapper roleMapper;
    public UserRegistrationMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public User toEntity(UserSaveDto userDto) {
        return new User.Builder()
                .withUsername(userDto.username())
                .withEmail(userDto.email())
                .withPassword(userDto.password())
                .withRoles(userDto.roles().stream()
                        .map(roleMapper::toEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

    public UserSaveDto toDto(User user) {
        return new UserSaveDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(roleMapper::toDto).collect(Collectors.toSet()));
    }
 }
