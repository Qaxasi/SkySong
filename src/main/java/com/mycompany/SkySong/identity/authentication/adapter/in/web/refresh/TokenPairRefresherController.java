//package com.mycompany.SkySong.identity.authentication.adapter.in.web.refresh;
//
//import com.mycompany.SkySong.infrastructure.http.ErrorView;
//import com.mycompany.SkySong.infrastructure.http.HttpErrorMapper;
//import com.mycompany.SkySong.infrastructure.security.jwt.JwtAccessTokenProperties;
//import com.mycompany.SkySong.infrastructure.security.refreshToken.RefreshTokenProperties;
//import com.mycompany.SkySong.identity.authentication.application.refresh.service.TokenPairRefresher;
//import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
//import com.mycompany.SkySong.shared.error.ErrorType;
//import com.mycompany.SkySong.infrastructure.http.response.BaseResponse;
//import com.mycompany.SkySong.infrastructure.http.response.SuccessResponse;
//import com.mycompany.SkySong.shared.result.Result;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/auth/token")
//public class TokenPairRefresherController {
//
//    private final CookieUtils cookieUtils;
//    private final TokenPairRefresher tokenPairRefresher;
//    private final JwtAccessTokenProperties accessTokenProperties;
//    private final RefreshTokenProperties refreshTokenProperties;
//    private final HttpErrorMapper errorMapper;
//
//    public TokenPairRefresherController(final CookieUtils cookieUtils,
//                                        final TokenPairRefresher tokenPairRefresher,
//                                        final JwtAccessTokenProperties accessTokenProperties,
//                                        final RefreshTokenProperties refreshTokenProperties,
//                                        final HttpErrorMapper errorMapper) {
//        this.cookieUtils = cookieUtils;
//        this.tokenPairRefresher = tokenPairRefresher;
//        this.accessTokenProperties = accessTokenProperties;
//        this.refreshTokenProperties = refreshTokenProperties;
//        this.errorMapper = errorMapper;
//    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<BaseResponse> refreshTokens(final HttpServletRequest request) {
//        return cookieUtils.getCookieValue(request, refreshTokenProperties.cookie().name())
//                .mapError((type, message) ->
//                        type == ErrorType.COOKIE_NOT_FOUND
//                                ? Result.failure("Invalid refresh token", ErrorType.INVALID_REFRESH_TOKEN)
//                                : Result.failure(message, type))
//                .flatMap(refreshToken -> tokenPairRefresher.refresh(refreshToken))
//                .fold(
//                        error -> errorMapper.from(ErrorView.fromResult(error)),
//
//                        tokens -> {
//                            final ResponseCookie accessTokenCookie = cookieUtils.generateCookie(
//                                    accessTokenProperties.cookie(), tokens.accessToken().value());
//
//                            final ResponseCookie refreshTokenCookie = cookieUtils.generateCookie(
//                                    refreshTokenProperties.cookie(), tokens.refreshToken().value());
//
//                            return ResponseEntity.ok()
//                                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
//                                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
//                                    .body(new SuccessResponse("Your session has been successfully extended."));
//                        });
//    }
//}
