package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDetailsAssertions {
    public static boolean hasAuthority(UserDetails userDetails, String authority) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(authority));
    }
    public static void assertPasswordMatches(UserDetails userDetails,
                                             PasswordEncoder passwordEncoder,
                                             String password) {
        assertTrue(passwordEncoder.matches(password, userDetails.getPassword()));
    }
}
