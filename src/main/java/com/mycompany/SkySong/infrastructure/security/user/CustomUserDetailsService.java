package com.mycompany.SkySong.security.user;

import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.User;
import com.mycompany.SkySong.infrastructure.persistence.sql.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public CustomUserDetailsService(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = findUserByUsername(usernameOrEmail);
        Set<GrantedAuthority> authorities = getAuthorities(user);

        return CustomUserDetails.build(user, authorities);
    }
    private User findUserByUsername(String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with username: %s", username)));
    }
    private  Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roles = roleDAO.findRolesByUserId(user.getId());

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toSet());
    }
}
