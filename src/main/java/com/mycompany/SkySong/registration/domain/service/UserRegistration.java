package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.domain.ports.UserSaver;

class UserRegistration {

    private final RequestValidation validation;
    private final UserCreator userCreator;
    private final UserSaver userSaver;

    UserRegistration(RequestValidation validation, UserCreator userCreator, UserSaver userSaver) {
        this.validation = validation;
        this.userCreator = userCreator;
        this.userSaver = userSaver;
    }
}
