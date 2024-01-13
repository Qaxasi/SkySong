package com.mycompany.SkySong.testsupport.auth.controller;

public class RegistrationHelper {
    public static final String validRequest =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"}";
    public static final String invalidRequest =
            "{\"username\": \"invalid\", \"email\": \"invalid\", \"password\": \"invalid\"}";
    public static final String malformedRequest =
            "{\"username\": \"New\", \"email\": \"new@mail.com\", \"password\": \"Password#3\"";
    public static final String emptyCredentials =
            "{\"username\": \"\", \"email\": \"\", \"password\": \"\"}";
}
