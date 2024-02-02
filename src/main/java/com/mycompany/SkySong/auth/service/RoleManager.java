package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;

public interface RoleManager {
    Role getRoleByName(UserRole userRole);
}
