package com.mycompany.SkySong.user.repository;

import com.mycompany.SkySong.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
