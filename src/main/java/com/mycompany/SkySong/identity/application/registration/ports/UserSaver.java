package com.mycompany.SkySong.application.user.registration.ports;

import com.mycompany.SkySong.identity.domain.User;

public interface UserSaver {
    void saveUser(User user);
}
