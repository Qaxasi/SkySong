package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

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
            b.initialized = true;
        }
        String roleCode = view.getColumn("roleCode", String.class);
        if (roleCode != null) {
            b.roleCodes.add(roleCode);
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
        final Set<String> roleCodes = new LinkedHashSet<>();

        UserAccessSnapshotView build() {
            return new UserAccessSnapshotView(userId, userTag, Set.copyOf(roleCodes));
        }

    }
}