package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.registration.domain.model.User;

public interface UserSaver {
    void saveUser(User user);
}
