package com.mycompany.SkySong.testutils.data;

import com.mycompany.SkySong.application.registration.dto.RegisterRequest;

public class RegistrationRequestData {
    public RegisterRequest validCredentials =
            new RegisterRequest("New", "new@mail.com", "Password#3");
    public RegisterRequest emptyCredentials =
            new RegisterRequest("", "", "");
    public RegisterRequest requestWithUsername(String username) {
        return new RegisterRequest(username, "new@mail.com", "Password#3");
    }
    public String malformedRequest =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";
    public RegisterRequest emailInvalidFormat =
            new RegisterRequest("Sam", "sam-sam.com", "Password#3");
}
