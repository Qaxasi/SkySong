package com.mycompany.SkySong.identity.adapter.in.registration.web.dto.mapper;

import com.mycompany.SkySong.identity.adapter.in.registration.web.dto.RegistrationRequest;
import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;

public class RegistrationRequestMapper {

    public UserRegistrationInput toDto(final RegistrationRequest request) {
        return new UserRegistrationInput(
                request.username(),
                request.email(),
                request.password());
    }
}
