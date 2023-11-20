package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.entity.Role;

public interface UserRoleManager {
    Role getRoleByName(String roleName);
}
