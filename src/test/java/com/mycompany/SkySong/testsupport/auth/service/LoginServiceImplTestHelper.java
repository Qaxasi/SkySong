package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.auth.service.LoginService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoginServiceImplTestHelper {
    public static String validLogin(AuthenticationManager authManager, Authentication auth,
                                          JwtTokenProvider tokenProvider, LoginService login) {

        LoginRequest loginRequest = new LoginRequest("User", "Password#3");
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("token");
        return login.login(loginRequest);
    }
}
