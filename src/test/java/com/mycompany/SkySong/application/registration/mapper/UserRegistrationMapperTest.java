package com.mycompany.SkySong.application.registration.mapper;

import com.mycompany.SkySong.application.registration.dto.RoleDTO;
import com.mycompany.SkySong.application.registration.dto.UserSaveDto;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRegistrationMapperTest {

    private RoleMapper roleMapper;
    private UserRegistrationMapper userMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapper();
        userMapper = new UserRegistrationMapper(roleMapper);
    }

    @Test
    void whenMappingDtoToEntity_EntityHasCorrectFields() {
        UserSaveDto userDto = createUserDto("Alex", "alex@mail.mail", "Password#3", UserRole.ROLE_USER);

        User user = toEntity(userDto);

        assertEquals("Alex", user.getUsername());
        assertEquals("alex@mail.mail", user.getEmail());
        assertEquals("Password#3", user.getPassword());
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals(UserRole.ROLE_USER)));
    }

    @Test
    void whenMappingEntityToDto_DtoHasCorrectFields() {
        User user = createUserEntity("Alex", "alex@mail.mail", "Password#3", UserRole.ROLE_USER);

        UserSaveDto userDto = toDto(user);

        assertEquals("Alex", userDto.username());
        assertEquals("alex@mail.mail", userDto.email());
        assertEquals("Password#3", userDto.password());
        assertTrue(userDto.roles().stream().anyMatch(r -> r.name().equals(UserRole.ROLE_USER)));
    }

    private UserSaveDto createUserDto(String username, String email, String password, UserRole name) {
        Role role = new Role(name);
        RoleDTO roleDTO = roleMapper.toDto(role);
        Set<RoleDTO> roles = Set.of(roleDTO);
        return new UserSaveDto(username, email, password, roles);
    }

    private User createUserEntity(String username, String email, String password, UserRole name) {
        Set<Role> roles = Set.of(new Role(name));
        return new User.Builder()
                .withUsername(username)
                .withEmail(email)
                .withPassword(password)
                .withRoles(roles)
                .build();
    }

    private UserSaveDto toDto(User user) {
        return userMapper.toDto(user);
    }

    private User toEntity(UserSaveDto userDTO) {
        return userMapper.toEntity(userDTO);
    }
}
