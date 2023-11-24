package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDetailsAssertions {
    public static boolean hasAuthority(UserDetails userDetails, String... authority) {
        Set<String> requiredAuthorities = new HashSet<>(Arrays.asList(authority));
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .containsAll(requiredAuthorities);
    }
    public static void assertUserDetails(UserDetails userDetails,
                                             PasswordEncoder passwordEncoder,
                                             String expectedUsername,
                                             String expectedPassword) {
        assertEquals(expectedUsername, userDetails.getUsername());
        assertTrue(passwordEncoder.matches(expectedPassword, userDetails.getPassword()));
    }
}
