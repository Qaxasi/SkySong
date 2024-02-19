package com.mycompany.SkySong.auth.utils;

public class ValidationPatterns {
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{3,20}$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]{1,15}@[a-zA-Z0-9.-]{2,15}\\.[a-zA-Z]{2,6}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";


}