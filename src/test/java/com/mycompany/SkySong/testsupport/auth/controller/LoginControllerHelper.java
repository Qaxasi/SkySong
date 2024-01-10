package com.mycompany.SkySong.testsupport.auth.controller;

public class LoginControllerHelper {
    public static final String validCredentials =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"}";
    public static final String invalidCredentials =
            "{\"usernameOrEmail\": \"invalid\",\"password\": \"invalid\"}";
    public static final String malformedJson =
            "{\"usernameOrEmail\": \"mail@mail.com\",\"password\": \"Password#3\"";
    public static final String emptyCredentials =
            "{\"usernameOrEmail\": \"\",\"password\": \"\"}";
    public static final String loginUri = "/api/v1/users/login";

}
