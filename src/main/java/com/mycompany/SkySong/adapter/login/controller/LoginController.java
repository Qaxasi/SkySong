package com.mycompany.SkySong.adapter.login.controller;

import com.mycompany.SkySong.adapter.login.dto.LoginResponse;
import com.mycompany.SkySong.adapter.login.service.LoginHandler;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.adapter.login.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final LoginHandler login;
    private final CookieUtils cookieUtils;

    public LoginController(LoginHandler login,
                           CookieUtils cookieUtils) {
        this.login = login;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse authResponse = login.login(request.usernameOrEmail(), request.password());

        ResponseCookie jwtCookie = cookieUtils.generateCookie(
                "jwtToken", authResponse.jwtToken(), "/api", 600);

        ResponseCookie refreshCookie = cookieUtils.generateCookie(
                "refreshToken", authResponse.refreshToken(), "/refresh-token", 86400);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new ApiResponse("Logged successfully."));
    }
}
