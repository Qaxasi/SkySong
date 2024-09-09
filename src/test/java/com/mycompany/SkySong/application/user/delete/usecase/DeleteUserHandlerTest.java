package com.mycompany.SkySong.application.user.delete.usecase;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.auth.common.UserIdFetcher;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteUserHandlerTest extends BaseIT {

    @Autowired
    private UserDeletionHandler userDeletion;
    private UserFixture userFixture;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserIdFetcher userIdFetcher;

    @BeforeEach
    void setup() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenUserDeleted_ReturnMessage() {
        userFixture.createUserWithUsername("Alex");
        int userId = userIdFetcher.fetchByUsername("Alex");
        ApiResponse response = userDeletion.delete(userId);
        String expectedMessage = String.format("User with ID %d deleted successfully.", userId);
        assertThat(response.message()).isEqualTo(expectedMessage);
    }
}
