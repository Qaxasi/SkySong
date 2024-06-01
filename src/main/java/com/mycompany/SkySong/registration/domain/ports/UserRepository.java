package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.common.dao.UserDAO;

public interface UserRepository extends UserDAO {
    boolean existsByUsername(String username);
    boolean existsByEmail( String email);
}
