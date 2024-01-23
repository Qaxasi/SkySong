package com.mycompany.SkySong.testsupport.auth.controller;

public class LoginRequests {
    public static final String validCredentials =
            "{\"usernameOrEmail\": \"User\",\"password\": \"Password#3\"}";
    public static final String invalidCredentials =
            "{\"usernameOrEmail\": \"invalid\",\"password\": \"invalid\"}";
    public static final String malformedJson =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";
    public static final String emptyCredentials =
            "{\"usernameOrEmail\": \"\",\"password\": \"\"}";
}
