package com.mycompany.SkySong.identity.registration.application.port;

import com.mycompany.SkySong.identity.registration.domain.User;
import com.mycompany.SkySong.shared.result.Result;

public interface UserSaver {
    Result<Void> saveUser(User user);
}
