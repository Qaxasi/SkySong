package com.mycompany.SkySong.adapter.registration.mapper;

import com.mycompany.SkySong.adapter.registration.dto.RegisterRequest;
import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;

public class RegisterRequestMapper {

    public UserRegistrationDto toDto(RegisterRequest request) {
        return new UserRegistrationDto(
                request.username(),
                request.email(),
                request.password());
    }
}
