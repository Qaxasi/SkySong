package com.mycompany.skysong.identity.adapter.out.security.spring;

import com.mycompany.skysong.identity.adapter.out.persistence.jdbi.UserAuthProjection;
import com.mycompany.skysong.identity.adapter.out.persistence.jdbi.UserIdentityDAO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserIdentityDAO dao;
    public CustomUserDetailsService(final UserIdentityDAO dao) {
        this.dao = dao;
    }
    @Override
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserAuthProjection projection = dao.findAuthByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                projection.userId(),
                projection.username(),
                projection.passwordHash());
    }
}
