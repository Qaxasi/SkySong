package com.mycompany.SkySong.auth.repository;

import com.mycompany.SkySong.auth.model.entity.Session;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface SessionDAO {

    @SqlUpdate("INSERT INTO sessions (session_id, user_id, create_at, expires_at) VALUES (:sessionId, :userId, :createAt, :expiresAt)")
    void insert(@Bind("sessionId") String sessionId, @Bind("userId") int userId,
                @Bind("createAt") Timestamp createAt, @Bind("expiresAt") Timestamp expiresAt);

    @SqlQuery("SELECT * FROM sessions WHERE session_id = :sessionId")
    @RegisterBeanMapper(Session.class)
    Optional<Session> findById(@Bind("sessionId") String sessionId);

    @SqlUpdate("DELETE FROM sessions WHERE session_id = :sessionId")
    void deleteById(@Bind("sessionId")String sessionId);
}
