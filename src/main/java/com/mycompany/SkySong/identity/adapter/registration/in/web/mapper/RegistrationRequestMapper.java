package com.mycompany.SkySong.identity.adapter.registration.in.web.mapper;

import com.mycompany.SkySong.identity.adapter.registration.in.web.dto.RegistrationRequest;
import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;

public class RegistrationRequestMapper {

    public UserRegistrationInput toDto(final RegistrationRequest request) {
        return new UserRegistrationInput(
                request.username(),
                request.email(),
                request.password());
    }
}
