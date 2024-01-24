package com.mycompany.SkySong.testsupport.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetailsServiceTestHelper {
    public static void assertUserHasAuthorities(UserDetails userDetails, String... authorities) {
        Set<String> requiredAuthorities = new HashSet<>(Arrays.asList(authorities));
        Set<String> userAuthorities =  userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (!userAuthorities.containsAll(requiredAuthorities)) {
            throw new AssertionError("User does not have all the required authorities. Expected: " +
                    Arrays.toString(authorities) + ", but has: " + userAuthorities);
        }
    }
    public static void assertUserDoesNotHaveAuthorities(UserDetails userDetails, String... authorities) {
        Set<String> forbiddenAuthorities = new HashSet<>(Arrays.asList(authorities));
        Set<String> userAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (String authority : forbiddenAuthorities) {
            if (userAuthorities.contains(authority)) {
                throw new AssertionError("User have forbidden authority: " + authority);
            }
        }
    }
}
