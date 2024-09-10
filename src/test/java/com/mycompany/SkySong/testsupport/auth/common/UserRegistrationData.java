package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;

public class UserRegistrationData {
    public UserRegistrationDto with(String username, String email, String password) {
        return new UserRegistrationDto(username, email, password);
    }
    public UserRegistrationDto withUsername(String username) {
        return new UserRegistrationDto(username, "new@mail.com", "Password#3");
    }
    public UserRegistrationDto withEmail(String email) {
        return new UserRegistrationDto("User", email, "Password#3");
    }
    public UserRegistrationDto requestWithPassword(String password) {
        return new UserRegistrationDto("User", "new@mail.com", password);
    }
    public UserRegistrationDto validData() {
        return new UserRegistrationDto("Alex", "alex@mail.mail", "Password#3");
    }
    public UserRegistrationDto passwordToShort =
            new UserRegistrationDto("Sam", "sam@mail.com", "Pass#3");

    public UserRegistrationDto passwordNoUppercaseLetter =
            new UserRegistrationDto("Sam", "sam@mail.com", "password#3");

    public UserRegistrationDto passwordNoLowercaseLetter =
            new UserRegistrationDto("Sam", "sam@mail.com", "PASSWORD#3");

    public UserRegistrationDto passwordNoSpecialCharacter =
            new UserRegistrationDto("Sam", "sam@mail.com", "Password3");

    public UserRegistrationDto passwordNoNumber =
            new UserRegistrationDto("Sam", "sam@mail.com", "Password#d");

    public UserRegistrationDto usernameToShort =
            new UserRegistrationDto("Sa", "sam@mail.com", "Password#3");

    public UserRegistrationDto usernameToLong =
            new UserRegistrationDto("testVeryLongUsernameFormat", "sam@mail.com", "Password#3");

    public UserRegistrationDto usernameWithSpecialCharacter =
            new UserRegistrationDto("Sam#", "sam@mail.com", "Password#3");

    public UserRegistrationDto emailInvalidFormat =
            new UserRegistrationDto("Sam", "sam-sam.com", "Password#3");

    public UserRegistrationDto emailToShort =
            new UserRegistrationDto("Sam", "s@m.c", "Password#3");

    public UserRegistrationDto emailToLong =
            new UserRegistrationDto("Sam", "qwertyuiopasdfghjklzxcvbn@mail.com", "Password#3");
}
