package com.mycompany.SkySong.adapter.identity.login.handler;

import com.mycompany.SkySong.adapter.identity.login.dto.LoginDto;
import com.mycompany.SkySong.adapter.identity.login.dto.LoginResponse;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserLoginService {

    private final AuthenticationManager authManager;
    private final JwtTokenManager jwtManager;

    public UserLoginService(AuthenticationManager authManager,
                            JwtTokenManager tokenManager) {
        this.authManager = authManager;
        this.jwtManager = tokenManager;
    }

    public LoginResponse login(LoginDto loginDto) {
        log.debug("Login attempt for username/email - {}", loginDto.usernameOrEmail());
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.usernameOrEmail(), loginDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            log.info("User '{}' (id:{}) authenticated successfully", userDetails.getUsername(), userDetails.id());

            String jwtToken = jwtManager.generateToken(userDetails);
            String refreshToken = jwtManager.generateRefreshToken(userDetails);

            return new LoginResponse(jwtToken, refreshToken);
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user/email - {}", loginDto.usernameOrEmail());
            throw new BadCredentialsException("Invalid username or password.");
        }
    }
}
