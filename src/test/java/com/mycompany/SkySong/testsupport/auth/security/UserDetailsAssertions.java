package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsAssertions {
    public static boolean hasAuthority(UserDetails userDetails, String authority) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(authority));
    }

}
