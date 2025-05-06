package com.mycompany.SkySong.adapter.user.registration.mapper;

import com.mycompany.SkySong.adapter.user.registration.dto.RegistrationRequest;
import com.mycompany.SkySong.application.user.registration.dto.UserRegistrationDto;

public class RegistrationRequestMapper {

    public UserRegistrationDto toDto(final RegistrationRequest request) {
        return new UserRegistrationDto(
                request.username(),
                request.email(),
                request.password());
    }
}
