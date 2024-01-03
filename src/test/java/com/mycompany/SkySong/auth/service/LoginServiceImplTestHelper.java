package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.auth.service.LoginServiceImpl;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginServiceImplTestHelper {
    private JwtTokenProvider tokenProvider;
    private AuthenticationManager authManager;
    private ApplicationMessageService message;
    private LoginService login;

    public LoginServiceImplTestHelper() {
        tokenProvider = Mockito.mock(JwtTokenProvider.class);
        authManager = Mockito.mock(AuthenticationManager.class);
        message = Mockito.mock(ApplicationMessageService.class);
        login = new LoginServiceImpl(tokenProvider, message, authManager);
    }
    public  String loginToken(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            when(authManager.authenticate(any())).thenReturn(authentication);
            when(tokenProvider.generateToken(authentication)).thenReturn("token");

            return login.login(loginRequest);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Exception");
        }
    }
}
