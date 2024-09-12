package com.mycompany.SkySong.adapter.security.user;

import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
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

    // This method loads a user by either username or email
    @Override
    public CustomUserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = findUserByUsernameOrEmail(usernameOrEmail);
        Set<GrantedAuthority> authorities = getAuthorities(user);

        return CustomUserDetails.build(user, authorities);
    }
    private User findUserByUsernameOrEmail(String usernameOrEmail) {
        return userDAO.findByUsername(usernameOrEmail)
                .orElseGet(() -> userDAO.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)));
    }
    private  Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roles = roleDAO.findRolesByUserId(user.getId());

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toSet());
    }
}
