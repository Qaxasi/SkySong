package com.mycompany.SkySong.testsupport.auth.controller;

public class RegistrationHelper {
    public static final String VALID_REQUEST =
            "{\"username\": \"User\", \"email\": \"mail@mail.com\", \"password\": \"Password#3\"}";
    public static final String INVALID_CREDENTIALS =
            "{\"username\": \"invalid\", \"email\": \"invalid\", \"password\": \"invalid\"}";
    public static final String MALFORMED_REQUEST =
            "{\"username\": \"User\", \"email\": \"mail@mail.com\", \"password\": \"Password#3\"";
    public static final String EMPTY_CREDENTIALS =
            "{\"username\": \"\", \"email\": \"\", \"password\": \"\"}";
}
