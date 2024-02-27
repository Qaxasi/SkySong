package com.mycompany.SkySong.auth.repository;

import com.mycompany.SkySong.auth.model.entity.Session;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface SessionDAO {

    @SqlUpdate("INSERT INTO sessions (session_id, user_id, create_at, expires_at) VALUES (:sessionId, :userId, :createAt, :expiresAt)")
    void save(@BindBean Session session);

    @SqlQuery("SELECT * FROM sessions WHERE session_id = :sessionId")
    @RegisterBeanMapper(Session.class)
    Optional<Session> findById(@Bind("sessionId") String sessionId);

    @SqlUpdate("DELETE FROM sessions WHERE session_id = :sessionId")
    void deleteById(@Bind("sessionId") String sessionId);

    @SqlUpdate("INSERT INTO user_sessions (user_id, session_id) VALUES (:userId, :sessionId)")
    void assignSessionToUser(@Bind("userId") int userId, @Bind("sessionId") String sessionId);
}
