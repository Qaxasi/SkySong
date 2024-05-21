package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.common.dao.UserDAO;

public interface UserRepositoryPort extends UserDAO {
    int save(User user);
    void assignRoleToUser(int userId, int roleId);
}
