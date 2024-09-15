package com.mycompany.SkySong.testutils.data;

import com.mycompany.SkySong.adapter.login.dto.LoginRequest;

public class LoginRequests {
    public LoginRequest nonExistingUser =
            new LoginRequest("Invalid", "invalid");
    public LoginRequest emptyCredentials =
            new LoginRequest("", "");

    public String malformedJson =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";

    public LoginRequest login(String username) {
        return new LoginRequest(username, "Password#3");
    }
}
