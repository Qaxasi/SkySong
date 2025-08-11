package com.mycompany.SkySong.identity.registration.application.port;

import com.mycompany.SkySong.identity.registration.domain.Role;
import com.mycompany.SkySong.identity.registration.domain.UserRole;
import com.mycompany.SkySong.shared.result.Result;

public interface RoleProvider {
    Result<Role> provideRole(UserRole role);
}
