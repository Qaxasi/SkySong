package com.mycompany.SkySong.adapter.user.delete.adapters;

import com.mycompany.SkySong.adapter.user.delete.adapters.exception.UserNotFoundException;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.auth.common.UserIdFetcher;
import com.mycompany.SkySong.testsupport.auth.security.RoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionDeleteUserTest extends BaseIT {
    @Autowired
    private TransactionDeleteUser deleteUser;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;
    private UserFixture userFixture;
    @Autowired
    private RoleChecker roleChecker;
    @Autowired
    private UserExistenceChecker userExistenceChecker;
    @Autowired
    private UserIdFetcher userIdFetcher;

    @BeforeEach
    void setup() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenUserNotFound_ThrowException() {
        assertThrows(UserNotFoundException.class, () -> deleteUser.deleteEverythingById(1));
    }

    @Test
    void whenUserDeleted_UserNotExist() {
        createUserWithUsername("Alex");
        int userId = fetchIdByUsername("Alex");

        deleteUser.deleteEverythingById(userId);

        assertThat(userExistenceChecker.userExist("Alex")).isFalse();
    }

    private int fetchIdByUsername(String username) {
        return userIdFetcher.fetchByUsername(username);
    }
    private void createUserWithUsername(String username) {
        userFixture.createUserWithUsername(username);
    }
}
