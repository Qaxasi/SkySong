package com.mycompany.SkySong.registration.domain.service;

class UserRegistrationService {

    private final UserRegistration registration;
    private final RequestValidation validation;

    UserRegistrationService(UserRegistration registration, RequestValidation validation) {
        this.registration = registration;
        this.validation = validation;
    }
}
