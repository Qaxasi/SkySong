package com.mycompany.SkySong.authentication.utils.constants;

public class ValidationPatterns {
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+([.]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]+([.][a-zA-Z0-9]+)+$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";


}
