package com.mycompany.SkySong.adapter.registration.adapters;

import com.mycompany.SkySong.application.registration.dto.RoleDTO;
import com.mycompany.SkySong.application.registration.dto.UserRegistrationDTO;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.testsupport.auth.common.UserCountChecker;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.service.UserRoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionUserSaverTest extends BaseIT {
    @Autowired
    private TransactionUserSaver userSaver;
    @Autowired
    private UserExistenceChecker userExistenceChecker;
    @Autowired
    private UserRoleChecker userRoleChecker;
    @Autowired
    private UserCountChecker userCountChecker;

    @Test
    void whenUserSaved_UserExist() {
        UserRegistrationDTO userDto = createUserDtoWithUsername("Alex");
        userSaver.saveUser(userDto);
        assertThat(userExistenceChecker.userExist("Alex")).isTrue();
    }

    @Test
    void whenUserSavedWithMultipleRoles_RolesAreAssignedToUser() {
        Set<RoleDTO> roles = Set.of(
                new RoleDTO(1, UserRole.ROLE_USER),
                new RoleDTO(2, UserRole.ROLE_ADMIN));

        UserRegistrationDTO userDto = createUserDtoWithRoles("Alex", roles);
        userSaver.saveUser(userDto);

        assertThat(userRoleChecker.hasUserRole("Alex", UserRole.ROLE_USER.name())).isTrue();
        assertThat(userRoleChecker.hasUserRole("Alex", UserRole.ROLE_ADMIN.name())).isTrue();
    }

    private UserRegistrationDTO createUserDtoWithUsername(String username) {
        RoleDTO roleDTO = new RoleDTO(1, UserRole.ROLE_USER);
        return new UserRegistrationDTO(username, "alex@mail.mail", "Password#3", Set.of(roleDTO));
    }

    private UserRegistrationDTO createUserDtoWithRoles(String username, Set<RoleDTO> roles) {
        return new UserRegistrationDTO(username, "alex@mail.mail", "Password#3", roles);
    }
}
