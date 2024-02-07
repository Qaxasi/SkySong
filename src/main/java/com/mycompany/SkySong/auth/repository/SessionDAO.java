package com.mycompany.SkySong.auth.repository;

import com.mycompany.SkySong.auth.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionDAO extends JpaRepository<Session, String> {
}
