package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;

public interface UserRoleManager {
    Role getRoleByName(UserRole userRole);
}
