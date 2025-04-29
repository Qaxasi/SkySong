package com.mycompany.SkySong.application.user.registration.mapper;

import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.application.user.registration.dto.UserSaveDto;

import java.util.stream.Collectors;

public class UserSaveMapper {
    private final RoleMapper roleMapper;
    public UserSaveMapper(final RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public User toEntity(final UserSaveDto userDto) {
        return new User.Builder()
                .withUsername(userDto.username())
                .withEmail(userDto.email())
                .withPassword(userDto.password())
                .withRoles(userDto.roles()
                        .stream()
                        .map(roleMapper::toEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

    public UserSaveDto toDto(final User user) {
        return new UserSaveDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(roleMapper::toDto)
                        .collect(Collectors.toSet()));
    }
 }
