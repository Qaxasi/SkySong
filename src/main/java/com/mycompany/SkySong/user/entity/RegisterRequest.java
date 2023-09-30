package com.mycompany.SkySong.user.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@Pattern(regexp = "^[a-zA-Z0-9]*$",
                              message = "The username can contain only letter and numbers.")
                              String username,
                              @Email(message = "Invalid email address format.")
                              String email,
                              @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                                      message = "The password must contain an least 8 characters, including " +
                                              "uppercase letters, lowercase letters, numbers, and special characters.")
                              String password) {
}
