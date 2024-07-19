package com.mycompany.SkySong.infrastructure.persistence.dao;

import com.mycompany.SkySong.domain.shared.entity.Session;
import com.mycompany.SkySong.domain.login.ports.LoginSessionRepository;
import com.mycompany.SkySong.domain.logout.ports.LogoutSessionRepository;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionDAO extends LoginSessionRepository, LogoutSessionRepository {

    @SqlUpdate("INSERT INTO sessions (session_id, user_id, create_at, expires_at) VALUES (:sessionId, :userId, :createAt, :expiresAt)")
    void save(@BindBean Session session);

    @SqlQuery("SELECT * FROM sessions WHERE session_id = :sessionId")
    @RegisterBeanMapper(Session.class)
    Optional<Session> findById(@Bind("sessionId") String sessionId);

    @SqlUpdate("DELETE FROM sessions WHERE session_id = :sessionId")
    void deleteById(@Bind("sessionId") String sessionId);

    @SqlUpdate("DELETE FROM sessions WHERE user_id = :userId")
    void deleteUserSessions(@Bind("userId") int userId);
}
