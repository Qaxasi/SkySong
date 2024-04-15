package com.mycompany.SkySong.common.security;

import com.mycompany.SkySong.user.Role;
import com.mycompany.SkySong.user.User;
import com.mycompany.SkySong.user.RoleDAO;
import com.mycompany.SkySong.user.UserDAO;
import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
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
    private final RoleDAO roleDAO;
    private final ApplicationMessageLoader message;

    public CustomUserDetailsService(UserDAO userDAO, RoleDAO roleDAO, ApplicationMessageLoader message) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.message = message;
    }

    // This method loads a user by either username or email
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = findUserByUsernameOrEmail(usernameOrEmail);
        Set<GrantedAuthority> authorities = getAuthorities(user);

        return new org.springframework.security.core.userdetails.User(usernameOrEmail,
                user.getPassword(),
                authorities);
    }
    private User findUserByUsernameOrEmail(String usernameOrEmail) {
        return userDAO.findByUsername(usernameOrEmail)
                .orElseGet(() -> userDAO.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                message.getMessage("user.not.found", usernameOrEmail))));
    }
    private  Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roles = roleDAO.findRolesByUserId(user.getId());

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toSet());
    }
}
