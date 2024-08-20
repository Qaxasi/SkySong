package com.mycompany.SkySong.adapter.login.controller;

import com.mycompany.SkySong.adapter.login.dto.AuthenticationResponse;
import com.mycompany.SkySong.adapter.login.service.AuthenticationHandler;
import com.mycompany.SkySong.adapter.security.jwt.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.login.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LoginController {

    private final AuthenticationHandler authentication;
    private final CookieUtils cookieUtils;

    public LoginController(AuthenticationHandler authentication,
                           CookieUtils cookieUtils) {
        this.authentication = authentication;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResponse authResponse = authenticationHandler.authenticate(request);

        ResponseCookie jwtCookie = generateCookie("jwtToken", authResponse.jwtToken(), "/api");

        ResponseCookie refreshCookie = generateCookie("refreshToken", authResponse.refreshToken(), "/api/auth/refresh-token");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new ApiResponse("Logged successfully."));
    }
}
