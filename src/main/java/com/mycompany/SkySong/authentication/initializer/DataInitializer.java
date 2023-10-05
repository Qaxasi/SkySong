package com.mycompany.SkySong.authentication.initializer;

import com.mycompany.SkySong.authentication.role.entity.Role;
import com.mycompany.SkySong.authentication.role.entity.UserRole;
import com.mycompany.SkySong.authentication.role.repository.RoleDAO;
import com.mycompany.SkySong.authentication.user.entity.User;
import com.mycompany.SkySong.authentication.user.repository.UserDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


@Configuration
public class DataInitializer {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner setupDefaultRoles(RoleDAO roleDAO) {
        return args -> {
            Role userRole = roleDAO.findByName(UserRole.ROLE_USER.name())
                    .orElseGet(() -> {
                        Role newUserRole = new Role(UserRole.ROLE_USER);
                        return roleDAO.save(newUserRole);
                    });
            Role adminRole = roleDAO.findByName(UserRole.ROLE_ADMIN.name())
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(UserRole.ROLE_ADMIN);
                        return roleDAO.save(newAdminRole);
                    });


        };
    }
    public void initAdminAccount() {
        String adminUsername = "admin";
        if (!userDAO.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("Admin12345*"));

            Role adminRole = roleDAO.findByName(UserRole.ROLE_ADMIN.toString())
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            admin.setRoles(Set.of(adminRole));
            userDAO.save(admin);
        }
    }

}
