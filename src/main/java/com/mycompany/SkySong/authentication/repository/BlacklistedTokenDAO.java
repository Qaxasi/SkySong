package com.mycompany.SkySong.authentication.repository;

import com.mycompany.SkySong.authentication.model.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenDAO extends JpaRepository<BlacklistedToken, Long> {
}
