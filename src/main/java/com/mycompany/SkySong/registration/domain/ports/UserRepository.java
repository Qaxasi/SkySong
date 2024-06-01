package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.common.dao.UserDAO;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface UserRepository extends UserDAO {
    int save(User user);
    void assignRoleToUser(int userId, int roleId);
    boolean existsByUsername(String username);
    boolean existsByEmail( String email);
}
