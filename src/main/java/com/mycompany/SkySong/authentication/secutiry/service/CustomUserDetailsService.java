package com.mycompany.SkySong.authentication.secutiry.service;

import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.impl.ApplicationMessageService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;
    private final ApplicationMessageService messageService;

    public CustomUserDetailsService(UserDAO userDAO, ApplicationMessageService messageService) {
        this.userDAO = userDAO;
        this.messageService = messageService;
    }

    // This method loads a user by either username or email
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(usernameOrEmail)
                .orElseGet(() -> userDAO.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                messageService.getMessage("user.not.found", usernameOrEmail))));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(usernameOrEmail,
                user.getPassword(),
                authorities);
    }
    private User findUserByUsernameOrEmail(String usernameOrEmail) {
        return userDAO.findByUsername(usernameOrEmail)
                .orElseGet(() -> userDAO.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                messageService.getMessage("user.not.found", usernameOrEmail))));
    }
}
