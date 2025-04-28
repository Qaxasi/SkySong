package com.mycompany.SkySong.adapter.user.registration.mapper;

import com.mycompany.SkySong.adapter.user.registration.dto.RegisterRequest;
import com.mycompany.SkySong.application.user.registration.dto.UserRegistrationDto;

public class RegisterRequestMapper {

    public UserRegistrationDto toDto(final RegisterRequest request) {
        return new UserRegistrationDto(
                request.username(),
                request.email(),
                request.password());
    }
}
