package com.mycompany.SkySong.identity.infrastructure.authentication.persistence.dao;

import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

public class UserAccessSnapshotReducer implements RowReducer<UserAccessSnapshotReducer.Builder, UserAccessSnapshotView> {

    @Override
    public Builder container() {
        return new Builder();
    }

    @Override
    public void accumulate(Builder b, RowView view) {
        if (!b.initialized) {
            b.userId = view.getColumn("userId", Integer.class);
            b.userTag = view.getColumn("userTag", String.class);
            b.accessVersion = view.getColumn("accessVersion", Integer.class);
            b.initialized = true;
        }
        String role = view.getColumn("roleCode", String.class);
        if (role != null) {
            b.roleCodes.add(role);
        }
    }

    @Override
    public Stream<UserAccessSnapshotView> stream(Builder b) {
        return b.initialized ? Stream.of(b.build()) : Stream.empty();
    }

    static final class Builder {
        boolean initialized;
        int userId;
        String userTag;
        int accessVersion;
        final Set<String> roleCodes = new LinkedHashSet<>();

        UserAccessSnapshotView build() {
            return new UserAccessSnapshotView(userId, userTag, accessVersion, roleCodes);
        }

    }
}
