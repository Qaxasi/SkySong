package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CustomUserDetailsService;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mycompany.SkySong.testsupport.common.UserTestConfigurator.*;

public class CustomUserDetailsServiceTestHelper {
    public static boolean assertUserHasAuthorities(UserDetails userDetails, String... authority) {
        Set<String> requiredAuthorities = new HashSet<>(Arrays.asList(authority));
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .containsAll(requiredAuthorities);
    }
    public static UserDetails setupAndLoadRegularUserByUsername(UserDAO userDAO,
                                                                CustomUserDetailsService detailsService) {
        User regularUser = createRegularUser();
        setupExistingUserByUsername(userDAO, regularUser);
        return detailsService.loadUserByUsername(regularUser.getUsername());
    }
    public static UserDetails setupAndLoadRegularUserByEmail(UserDAO userDAO,
                                                             CustomUserDetailsService detailsService) {
        User regularUser = createRegularUser();
        setupExistingUserByEmail(userDAO, regularUser);
        return detailsService.loadUserByUsername(regularUser.getEmail());
    }
    public static UserDetails setupAndLoadAdminUserByUsername(UserDAO userDAO,
                                                              CustomUserDetailsService detailsService) {
        User adminUser = createAdminUser();
        setupExistingUserByUsername(userDAO, adminUser);
        return detailsService.loadUserByUsername(adminUser.getUsername());
    }
    public static UserDetails setupAndLoadAdminUserByEmail(UserDAO userDAO,
                                                              CustomUserDetailsService detailsService) {
        User adminUser = createAdminUser();
        setupExistingUserByEmail(userDAO, adminUser);
        return detailsService.loadUserByUsername(adminUser.getEmail());
    }
}
