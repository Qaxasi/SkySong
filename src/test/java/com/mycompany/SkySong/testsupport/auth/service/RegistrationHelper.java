package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;

public class RegistrationHelper {
    public static RegisterRequest newUserRegisterRequest(RegistrationService registrationService) throws DatabaseException {
        String username = "testUniqueUsername";
        String email = "testUniqueEmail@gmail.com";
        String password = "testPassword@123";
        return new RegisterRequest(username, email, password);
    }
}
