package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.identity.application.user.authentication.model.UserAccessSnapshot;
import com.mycompany.skysong.identity.application.user.authentication.port.UserAccessSnapshotReader;
import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.identity.domain.UserRole;
import com.mycompany.skysong.identity.domain.UserTag;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
class JdbiUserAccessSnapshotReader implements UserAccessSnapshotReader {
    private static final Logger log = LoggerFactory.getLogger(JdbiUserAccessSnapshotReader.class);
    private final UserIdentityDAO dao;
    JdbiUserAccessSnapshotReader(final UserIdentityDAO dao) {
        this.dao = dao;
    }

    @Override
    public Result<UserAccessSnapshot> read(final UserId userId) {
        try {
            return dao.findUserAccessSnapshotByUserId(userId.asInt())
                    .map(projection -> Result.combine(
                            UserTag.fromStored(projection.userTag()),
                            mapRoleCodesToUserRoles(projection.roles()),

                            UserAccessSnapshot::new))
                    .orElseGet(() -> {
                        log.error("Missing access snapshot {} {}",
                                kv("op", "user_access_snapshot.read"),
                                kv("userId", userId.asInt()));

                        return Result.failure("User access snapshot missing", ErrorType.ACCESS_SNAPSHOT_NOT_FOUND);
                    });
        } catch (JdbiException ex) {
            log.error("sql read failed {}",
                    kv("op", "user_access_snapshot.read"),
                    ex);
            return Result.failure("Persistence error", ErrorType.PERSISTENCE_ERROR);
        }
    }

    private Result<Set<UserRole>> mapRoleCodesToUserRoles(final Set<String> codes) {
        final Set<String> source = (codes != null) ? codes : Collections.emptySet();

        Result<EnumSet<UserRole>> acc = Result.success(EnumSet.noneOf(UserRole.class));
        for (final String code : source) {
            acc = Result.combine(
                    acc,
                    UserRole.fromStored(code),
                    (set, role) -> {
                        set.add(role);
                        return set;
                    });
        }
        return acc.map(s -> Collections.unmodifiableSet(EnumSet.copyOf(s)));
    }
}