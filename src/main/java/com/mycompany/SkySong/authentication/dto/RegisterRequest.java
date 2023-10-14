package com.mycompany.SkySong.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@Pattern(regexp = "^[a-zA-Z0-9]*$",
                              message = "The username can contain only letter and numbers.")
                              @NotNull(message = "The username field cannot be null.")
                              @NotEmpty(message = "The username field cannot be empty.")
                              String username,
                              @Email(message = "Invalid email address format.")
                              @NotNull(message = "The email field cannot be null")
                              @NotEmpty(message = "The email field cannot be empty")
                              String email,
                              @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                                      message = "Invalid password format. The password must contain an least 8 " +
                                                "characters, including uppercase letters, lowercase letters, numbers," +
                                                " and special characters.")
                              @NotNull(message = "The password field cannot be null")
                              @NotEmpty(message = "The password field cannot be empty")
                              String password) {
}
