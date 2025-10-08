package com.mycompany.SkySong.infrastructure.security.user;

import com.mycompany.SkySong.identity.infrastructure.persistence.dao.auth.UserAuthDAO;

import com.mycompany.SkySong.identity.infrastructure.persistence.dao.auth.UserAuthView;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAuthDAO userAuthDAO;
    public CustomUserDetailsService(UserAuthDAO userAuthDAO) {
        this.userAuthDAO = userAuthDAO;
    }
    @Override
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserAuthView view = userAuthDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                view.userId(),
                view.username(),
                view.passwordHash(),
                !view.locked(),
                view.enabled());
    }
}
