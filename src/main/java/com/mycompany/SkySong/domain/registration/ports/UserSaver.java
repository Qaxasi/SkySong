package com.mycompany.SkySong.application.registration.port.out;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDTO;
import com.mycompany.SkySong.domain.shared.entity.User;

public interface UserSaver {
    void saveUser(User user);
}
