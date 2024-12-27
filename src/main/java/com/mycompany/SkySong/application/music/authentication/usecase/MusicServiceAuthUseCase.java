package com.mycompany.SkySong.application.music.authentication.usecase;

import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.domain.music.authentication.port.MusicServiceAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MusicServiceAuthUseCase {
    private final MusicServiceAuthenticator authentication;

    public MusicServiceAuthUseCase(MusicServiceAuthenticator authentication) {
        this.authentication = authentication;
    }

    public String authenticateUser(int userId, AuthParams params) {
        return authentication.authenticateAndReturnToken(userId, params);
    }
}

