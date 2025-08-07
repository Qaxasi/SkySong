package com.mycompany.SkySong.identity.application.registration.ports;

import com.mycompany.SkySong.identity.domain.User;

public interface UserSaver {
    void saveUser(User user);
}
