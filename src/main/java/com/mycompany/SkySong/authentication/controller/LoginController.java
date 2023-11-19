package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.model.dto.ApiResponse;
import com.mycompany.SkySong.authentication.model.dto.JWTAuthResponse;
import com.mycompany.SkySong.authentication.model.dto.LoginRequest;
import com.mycompany.SkySong.authentication.service.CookieService;
import com.mycompany.SkySong.authentication.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LoginController {
    private final LoginService loginService;
    private final CookieService cookieService;
    public LoginController(LoginService loginService, CookieService cookieService) {
        this.loginService = loginService;
        this.cookieService = cookieService;
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        String token = loginService.login(loginRequest);
        ApiResponse jwtAuthResponse = new ApiResponse(token);

        cookieService.addCookie(response, "auth_token", token, 24 * 60 * 60);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
