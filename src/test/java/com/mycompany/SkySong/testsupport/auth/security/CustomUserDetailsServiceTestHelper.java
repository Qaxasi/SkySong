package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetailsServiceTestHelper {
    public static boolean assertUserHasAuthorities(UserDetails userDetails, String... authority) {
        Set<String> requiredAuthorities = new HashSet<>(Arrays.asList(authority));
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .containsAll(requiredAuthorities);
    }
}
