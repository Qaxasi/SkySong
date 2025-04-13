package com.mycompany.SkySong.application.music.authentication.port;

import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.shared.result.Result;

public interface MusicServiceAuthenticator {
    Result<String> authenticateAndReturnToken(int userId, AuthParams params);
}
