package com.mycompany.SkySong.application.music.authentication.usecase;

import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.domain.music.authentication.port.MusicServiceAuthenticator;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticateMusicUser {
    private final MusicServiceAuthenticator authentication;

    public AuthenticateMusicUser(MusicServiceAuthenticator authentication) {
        this.authentication = authentication;
    }

    public Result<String> execute(int userId, AuthParams params) {
        return authentication.authenticateAndReturnToken(userId, params);
    }
}

