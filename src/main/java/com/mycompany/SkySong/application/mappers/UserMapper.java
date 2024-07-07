package com.mycompany.SkySong.common.mapper;

import com.mycompany.SkySong.infrastructure.entity.User;
import com.mycompany.SkySong.common.dto.UserDTO;

public class UserMapper {
    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        return user;
    }
    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail());
    }
}
