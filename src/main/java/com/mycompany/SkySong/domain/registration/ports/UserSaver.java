package com.mycompany.SkySong.domain.registration.ports;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDTO;

public interface UserSaver {
    void saveUser(UserRegistrationDTO userDto);
}
