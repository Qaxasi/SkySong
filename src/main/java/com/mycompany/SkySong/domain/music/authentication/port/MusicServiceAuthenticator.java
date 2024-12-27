package com.mycompany.SkySong.domain.music.authentication.port;

import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;

public interface MusicServiceAuthenticator {
    String authenticateAndReturnToken(int userId, AuthParams params);
}
