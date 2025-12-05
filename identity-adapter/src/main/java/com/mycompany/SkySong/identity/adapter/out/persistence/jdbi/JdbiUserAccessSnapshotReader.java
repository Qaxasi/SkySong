package com.mycompany.SkySong.identity.adapter.out.persistence.jdbi;

import com.mycompany.SkySong.identity.application.dto.UserAccessSnapshot;
import com.mycompany.SkySong.identity.application.port.UserAccessSnapshotReader;
import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.identity.domain.UserRole;
import com.mycompany.skysong.identity.domain.UserTag;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@Transactional(readOnly = true)
class JdbiUserAccessSnapshotReader implements UserAccessSnapshotReader {
    private static final Logger log = LoggerFactory.getLogger(JdbiUserAccessSnapshotReader.class);
    private final UserIdentityDAO dao;
    JdbiUserAccessSnapshotReader(final UserIdentityDAO dao) {
        this.dao = dao;
    }

    @Override
    public Result<UserAccessSnapshot> load(final UserId userId) {
        try {
            return dao.findAccessSnapshotByUserId(userId.asInt())
                    .map(view -> Result.combine(
                            UserTag.fromStored(view.userTag()),
                            mapRoleCodesToUserRoles(view.roles()),

                            UserAccessSnapshot::new))
                    .orElseGet(() -> {
                        log.error("Missing access snapshot {} {}",
                                kv("op", "access_snapshot.load"),
                                kv("userId", userId.asInt()));

                        return Result.failure("User access snapshot missing", ErrorType.ACCESS_SNAPSHOT_NOT_FOUND);
                    });
        } catch (DataAccessException ex) {
            log.error("sql read failed {}",
                    kv("op", "access_snapshot.load"),
                    ex);
            return Result.failure("Internal persistence error", ErrorType.PERSISTENCE_ERROR);
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