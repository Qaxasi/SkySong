package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.UserRole;

public interface UserRoleManager {
    Role getRoleByName(UserRole userRole);
}
