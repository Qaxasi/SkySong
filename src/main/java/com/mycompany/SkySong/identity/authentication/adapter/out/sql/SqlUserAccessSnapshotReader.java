package com.mycompany.SkySong.identity.authentication.adapter.out.sql;

import com.mycompany.SkySong.identity.authentication.application.login.dto.UserAccessSnapshot;
import com.mycompany.SkySong.identity.authentication.application.login.port.UserAccessSnapshotReader;
import com.mycompany.SkySong.identity.authentication.domain.AccessVersion;
import com.mycompany.SkySong.identity.infrastructure.authentication.persistence.dao.UserAccessSnapshotView;
import com.mycompany.SkySong.identity.infrastructure.authentication.persistence.dao.UserIdentityDAO;
import com.mycompany.SkySong.identity.shared.domain.UserId;
import com.mycompany.SkySong.identity.shared.domain.UserRole;
import com.mycompany.SkySong.identity.shared.domain.UserTag;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional(readOnly = true)
class SqlUserAccessSnapshotReader implements UserAccessSnapshotReader {
    private final UserIdentityDAO dao;
    SqlUserAccessSnapshotReader(final UserIdentityDAO dao) {
        this.dao = dao;
    }

    @Override
    public Result<UserAccessSnapshot> load(final UserId userId) {
        final Optional<UserAccessSnapshotView> viewOpt = dao.findAccessSnapshotByUserId(userId.asInt());
        if (viewOpt.isEmpty()) {
            return Result.failure("User access snapshot not found", ErrorType.ACCESS_SNAPSHOT_NOT_FOUND);
        }

        final UserAccessSnapshotView view = viewOpt.get();

        final Result<UserTag> userTagRes = UserTag.of(view.userTag());
        final Result<AccessVersion> accessVersionRes = AccessVersion.of(view.accessVersion());
        final Result<Set<UserRole>> rolesRes = mapRoleCodesToUserRoles(view.roles());

        return Result.combine(userTagRes, accessVersionRes, rolesRes, UserAccessSnapshot::new)
                .mapError("User access snapshot contains invalid data", ErrorType.DATA_INTEGRITY_ERROR);
    }

    private Result<Set<UserRole>> mapRoleCodesToUserRoles(final Set<String> codes) {
        final Set<String> source = (codes != null) ? codes : Collections.emptySet();
        final EnumSet<UserRole> set = EnumSet.noneOf(UserRole.class);
        for (final String code : source) {
            final Result<UserRole> roleRes = UserRole.fromCode(code);
            if (roleRes.isFailure()) {
                return roleRes.propagateFailure();
            }
            set.add(roleRes.get());
        }
        return Result.success(Collections.unmodifiableSet(set));
    }
}