package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.registration.domain.model.User;

public interface UserRepositoryPort {

    int save(User user);
    void assignRoleToUser(int userId, int roleId);
}
