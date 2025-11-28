package com.mycompany.SkySong.identity.a.adapter.out.springsecurity;

import com.mycompany.SkySong.identity.a.adapter.out.persistence.jdbi.UserIdentityDAO;

import com.mycompany.SkySong.identity.a.adapter.out.persistence.jdbi.UserAuthView;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserIdentityDAO userAuthDAO;
    public CustomUserDetailsService(UserIdentityDAO userAuthDAO) {
        this.userAuthDAO = userAuthDAO;
    }
    @Override
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserAuthView view = userAuthDAO.findAuthCredentialsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                view.userId(),
                view.username(),
                view.passwordHash(),
                !view.locked(),
                view.enabled());
    }
}
