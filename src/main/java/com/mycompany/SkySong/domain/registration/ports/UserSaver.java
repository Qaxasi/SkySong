package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.registration.dto.UserRegistrationDTO;

public interface UserSaver {
    void saveUser(UserRegistrationDTO user);
}
